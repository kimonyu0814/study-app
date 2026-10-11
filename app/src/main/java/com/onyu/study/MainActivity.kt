package com.onyu.study

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONObject

/** 학습기록 · 정답확인 두 화면을 아래 탭으로 오가는 앱 */
class MainActivity : Activity() {

    private val pages = arrayOf("index.html", "check.html")
    private val names = arrayOf("학습기록", "정답확인")
    private lateinit var webs: Array<WebView>
    private lateinit var tabs: Array<TextView>
    private var cur = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL

        val frame = FrameLayout(this)
        webs = Array(pages.size) { i -> makeWeb(i) }
        webs.forEach { w ->
            frame.addView(w, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
        root.addView(frame, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.setBackgroundColor(Color.parseColor("#1F2433"))
        tabs = Array(names.size) { i ->
            val t = TextView(this)
            t.text = names[i]
            t.gravity = Gravity.CENTER
            t.textSize = 15f
            t.setPadding(0, dp(14), 0, dp(14))
            t.setOnClickListener { select(i) }
            t
        }
        tabs.forEach { t ->
            bar.addView(t, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        }
        root.addView(bar, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setContentView(root)

        cur = getSharedPreferences("study", MODE_PRIVATE).getInt("tab", 0).coerceIn(0, pages.size - 1)
        select(cur)

        Notify.channel(this)
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }
        AlarmReceiver.schedule(this)
        TodayWidget.refresh(this)
        checkUpdate()
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private fun makeWeb(i: Int): WebView {
        val w = WebView(this)
        w.settings.javaScriptEnabled = true
        w.settings.domStorageEnabled = true
        w.settings.cacheMode = WebSettings.LOAD_DEFAULT
        w.webViewClient = object : WebViewClient() {
            private fun fallback(view: WebView, req: WebResourceRequest) {
                // 인터넷이 안 되거나 페이지가 아직 없으면 앱 안에 넣어 둔 사본을 띄움
                if (req.isForMainFrame && !req.url.toString().startsWith("file:")) {
                    view.loadUrl("file:///android_asset/" + pages[i])
                }
            }
            override fun onReceivedError(view: WebView, req: WebResourceRequest, err: WebResourceError) = fallback(view, req)
            override fun onReceivedHttpError(view: WebView, req: WebResourceRequest, res: WebResourceResponse) {
                if (res.statusCode >= 400) fallback(view, req)
            }
            override fun shouldOverrideUrlLoading(view: WebView, req: WebResourceRequest): Boolean {
                val u = req.url.toString()
                if (u.startsWith(Study.PAGES) || u.startsWith("file:")) return false
                return try {
                    startActivity(Intent(Intent.ACTION_VIEW, req.url))
                    true
                } catch (e: Exception) { false }
            }
        }
        w.visibility = View.GONE
        // 켤 때마다 최신 화면을 받도록 주소 뒤에 시각을 붙임 (안 되면 앱 안 사본)
        w.loadUrl(Study.PAGES + pages[i] + "?t=" + System.currentTimeMillis())
        return w
    }

    private fun select(i: Int) {
        cur = i
        webs.forEachIndexed { k, w -> w.visibility = if (k == i) View.VISIBLE else View.GONE }
        tabs.forEachIndexed { k, t ->
            t.setTextColor(if (k == i) Color.WHITE else Color.parseColor("#8A8FA0"))
            t.typeface = if (k == i) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
        getSharedPreferences("study", MODE_PRIVATE).edit().putInt("tab", i).apply()
    }

    override fun onResume() {
        super.onResume()
        if (!::webs.isInitialized) return
        webs.forEach { w ->
            w.onResume()
            // 다른 기기에서 바꾼 내용 바로 불러오기
            w.evaluateJavascript("try{pull()}catch(e){};try{gasPull()}catch(e){}", null)
        }
    }

    override fun onPause() {
        super.onPause()
        if (::webs.isInitialized) TodayWidget.refresh(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val w = webs[cur]
        if (w.canGoBack()) w.goBack() else super.onBackPressed()
    }

    /** GitHub 에 새 버전이 올라왔는지 확인 */
    private fun checkUpdate() {
        Thread {
            try {
                val j = JSONObject(Study.httpGet("https://api.github.com/repos/${Study.REPO}/releases/latest"))
                val latest = j.optString("tag_name").removePrefix("v").toIntOrNull() ?: 0
                val assets = j.optJSONArray("assets")
                var url: String? = null
                if (assets != null) {
                    for (k in 0 until assets.length()) {
                        val a = assets.optJSONObject(k) ?: continue
                        if (a.optString("name").endsWith(".apk")) url = a.optString("browser_download_url")
                    }
                }
                val apk = url
                if (latest > BuildConfig.VERSION_CODE && apk != null) {
                    runOnUiThread {
                        if (!isFinishing) {
                            AlertDialog.Builder(this)
                                .setTitle("새 버전이 있습니다")
                                .setMessage("지금 v${BuildConfig.VERSION_CODE} → 새 버전 v$latest\n\n'업데이트'를 누르면 새 APK를 내려받습니다. 다 받은 뒤 눌러서 설치하면 기록은 그대로 유지됩니다.")
                                .setPositiveButton("업데이트") { _, _ ->
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(apk)))
                                }
                                .setNegativeButton("나중에", null)
                                .show()
                        }
                    }
                }
            } catch (e: Exception) {
                // 인터넷이 안 되면 다음에 다시 확인
            }
        }.start()
    }
}
