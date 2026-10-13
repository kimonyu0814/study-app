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
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.net.Uri as AUri
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONObject

/** 처음엔 식별 코드 입력, 그다음 사람마다 다른 탭 */
class MainActivity : Activity() {

    class Tab(val key: String, val name: String, val page: String)

    private val tabsByRole = mapOf(
        "0000" to listOf(
            Tab("study", "학습기록", "index.html"),
            Tab("check", "정답확인", "check.html"),
            Tab("schedule", "스케줄", "family.html#tab=schedule"),
            Tab("admin", "관리", "family.html#tab=admin"),
            Tab("settings", "설정", "family.html#tab=settings")
        ),
        "0001" to listOf(
            Tab("study", "학습기록", "index.html"),
            Tab("schedule", "스케줄", "family.html#tab=schedule"),
            Tab("msg", "채팅", "family.html#tab=msg"),
            Tab("settings", "설정", "family.html#tab=settings")
        ),
        "0003" to listOf(
            Tab("schedule", "시간표", "family.html#tab=schedule"),
            Tab("msg", "채팅", "family.html#tab=msg"),
            Tab("settings", "설정", "family.html#tab=settings")
        ),
        "0002" to listOf(
            Tab("study", "학습기록", "index.html"),
            Tab("schedule", "스케줄", "family.html#tab=schedule"),
            Tab("msg", "채팅", "family.html#tab=msg"),
            Tab("settings", "설정", "family.html#tab=settings")
        )
    )
    private var tabs: List<Tab> = emptyList()
    private val webs = ArrayList<WebView>()
    private val buttons = ArrayList<TextView>()
    private var cur = 0
    /* 채팅: 사진 고르기 · 마이크 */
    private var fileCb: ValueCallback<Array<AUri>>? = null
    private var micReq: PermissionRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Notify.channel(this)
        if (Me.role(this).isEmpty()) showLogin() else showMain(intent?.getStringExtra("tab"))
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    // ---------- 처음 화면: 식별 코드 ----------
    private fun showLogin() {
        val box = LinearLayout(this)
        box.orientation = LinearLayout.VERTICAL
        box.gravity = Gravity.CENTER_HORIZONTAL
        box.setPadding(dp(28), dp(96), dp(28), dp(28))
        box.setBackgroundColor(Color.parseColor("#FBFAF7"))

        val title = TextView(this)
        title.text = "학습기록"
        title.textSize = 30f
        title.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        title.setTextColor(Color.parseColor("#1F2433"))
        val sub = TextView(this)
        sub.text = "식별 코드를 입력하세요"
        sub.textSize = 16f
        sub.setTextColor(Color.parseColor("#6E7180"))
        sub.setPadding(0, dp(10), 0, dp(24))

        val edit = EditText(this)
        edit.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        edit.filters = arrayOf(InputFilter.LengthFilter(4))
        edit.textSize = 26f
        edit.gravity = Gravity.CENTER
        edit.letterSpacing = 0.4f
        edit.imeOptions = EditorInfo.IME_ACTION_DONE
        edit.setTextColor(Color.parseColor("#1F2433"))

        val err = TextView(this)
        err.textSize = 14f
        err.setTextColor(Color.parseColor("#C2372D"))
        err.gravity = Gravity.CENTER
        err.setPadding(0, dp(10), 0, dp(10))

        val ok = Button(this)
        ok.text = "확인"
        ok.textSize = 17f
        ok.setTextColor(Color.WHITE)
        ok.setBackgroundColor(Color.parseColor("#1F2433"))

        var askPw = false
        val submit = {
            val v = edit.text.toString().trim()
            if (!askPw) {
                when {
                    v.isEmpty() -> err.text = "코드를 입력하세요."
                    !Me.PEOPLE.containsKey(v) -> { err.text = "등록되지 않은 코드입니다."; edit.setText("") }
                    v == "0000" -> {
                        askPw = true
                        sub.text = "온유 · 비밀번호를 입력하세요"
                        err.text = ""
                        edit.setText("")
                        edit.filters = arrayOf(InputFilter.LengthFilter(20))
                    }
                    else -> { Me.set(this, v, ""); afterLogin() }
                }
            } else if (v.isEmpty()) {
                err.text = "비밀번호를 입력하세요."
            } else {
                ok.isEnabled = false
                err.setTextColor(Color.parseColor("#6E7180"))
                err.text = "확인 중…"
                Thread {
                    val res: Boolean? = try { Family.auth(v) } catch (e: Exception) { null }
                    runOnUiThread {
                        ok.isEnabled = true
                        err.setTextColor(Color.parseColor("#C2372D"))
                        when (res) {
                            true -> { Me.set(this, "0000", v); afterLogin() }
                            false -> { err.text = "비밀번호가 맞지 않습니다."; edit.setText("") }
                            null -> err.text = "인터넷 연결을 확인한 뒤 다시 눌러 주세요."
                        }
                    }
                }.start()
            }
        }
        ok.setOnClickListener { submit() }
        edit.setOnEditorActionListener { _, id, _ -> if (id == EditorInfo.IME_ACTION_DONE) { submit(); true } else false }

        val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
        val match = ViewGroup.LayoutParams.MATCH_PARENT
        box.addView(title, LinearLayout.LayoutParams(wrap, wrap))
        box.addView(sub, LinearLayout.LayoutParams(wrap, wrap))
        box.addView(edit, LinearLayout.LayoutParams(dp(220), wrap))
        box.addView(err, LinearLayout.LayoutParams(match, wrap))
        box.addView(ok, LinearLayout.LayoutParams(dp(220), dp(52)))
        setContentView(box)
        edit.requestFocus()
    }

    private fun afterLogin() {
        currentFocus?.let { v ->
            getSystemService(InputMethodManager::class.java).hideSoftInputFromWindow(v.windowToken, 0)
        }
        Family.register(this)
        AlarmReceiver.scheduleAll(this)
        showMain(null)
    }

    // ---------- 본 화면 ----------
    private fun showMain(openTab: String?) {
        val role = Me.role(this)
        tabs = tabsByRole[role] ?: tabsByRole.getValue("0001")
        webs.clear()
        buttons.clear()

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        val frame = FrameLayout(this)
        tabs.forEachIndexed { i, _ ->
            val w = makeWeb(i)
            webs.add(w)
            frame.addView(w, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
        root.addView(frame, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.setBackgroundColor(Color.parseColor("#1F2433"))
        tabs.forEachIndexed { i, t ->
            val b = TextView(this)
            b.text = t.name
            b.gravity = Gravity.CENTER
            b.textSize = if (tabs.size > 4) 13f else 15f
            b.setPadding(0, dp(14), 0, dp(14))
            b.setOnClickListener { select(i) }
            buttons.add(b)
            bar.addView(b, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        }
        root.addView(bar, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setContentView(root)

        val saved = getSharedPreferences("study", MODE_PRIVATE).getString("tabKey", "") ?: ""
        val want = tabs.indexOfFirst { t -> t.key == (openTab ?: saved) }
        select(if (want >= 0) want else 0)

        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }
        AlarmReceiver.scheduleAll(this)
        Family.register(this)
        if (role == "0000") TodayWidget.refresh(this)
        checkUpdate()
        checkChanges()
    }

    /** 새로 바뀐 점 (docs/changes.json) — 처음 보는 것만 한 번 보여 줌 */
    private fun checkChanges() {
        val role = Me.role(this)
        Thread {
            try {
                val arr = org.json.JSONArray(Study.httpGet(Study.PAGES + "changes.json?t=" + System.currentTimeMillis()))
                if (arr.length() == 0) return@Thread
                val sp = getSharedPreferences("study", MODE_PRIVATE)
                val seen = sp.getString("seenChange", "") ?: ""
                val newest = arr.optJSONObject(0)?.optString("id") ?: return@Thread
                if (newest == seen) return@Thread
                fun forMe(o: JSONObject): Boolean {
                    val w = o.optJSONArray("who") ?: return true
                    for (i in 0 until w.length()) if (w.optString(i) == role) return true
                    return false
                }
                val sb = StringBuilder()
                for (i in 0 until arr.length()) {
                    val c = arr.optJSONObject(i) ?: continue
                    if (c.optString("id") == seen) break
                    if (!forMe(c)) continue
                    val items = c.optJSONArray("items") ?: continue
                    val lines = ArrayList<String>()
                    for (k in 0 until items.length()) {
                        val it = items.optJSONObject(k) ?: continue
                        if (forMe(it)) lines.add("• " + it.optString("t"))
                    }
                    if (lines.isEmpty()) continue
                    if (sb.isNotEmpty()) sb.append("\n\n")
                    sb.append(c.optString("title")).append("\n").append(lines.joinToString("\n"))
                    if (seen.isEmpty()) break      // 처음 설치면 가장 최근 것만
                }
                runOnUiThread {
                    sp.edit().putString("seenChange", newest).apply()
                    if (sb.isNotEmpty() && !isFinishing) {
                        AlertDialog.Builder(this)
                            .setTitle("새로 바뀐 점")
                            .setMessage(sb.toString())
                            .setPositiveButton("확인", null)
                            .show()
                    }
                }
            } catch (e: Exception) {
            }
        }.start()
    }

    private fun makeWeb(i: Int): WebView {
        val page = tabs[i].page
        val w = WebView(this)
        w.settings.javaScriptEnabled = true
        w.settings.domStorageEnabled = true
        w.settings.cacheMode = WebSettings.LOAD_DEFAULT
        w.addJavascriptInterface(Bridge(), "App")
        w.settings.mediaPlaybackRequiresUserGesture = false
        w.webChromeClient = object : WebChromeClient() {
            // 📷 사진 고르기 (앨범 · 카메라)
            override fun onShowFileChooser(view: WebView, cb: ValueCallback<Array<AUri>>, params: FileChooserParams): Boolean {
                fileCb?.onReceiveValue(null)
                fileCb = cb
                return try {
                    val pick = Intent(Intent.ACTION_GET_CONTENT).addCategory(Intent.CATEGORY_OPENABLE).setType("image/*")
                    startActivityForResult(Intent.createChooser(pick, "사진 고르기"), 41)
                    true
                } catch (e: Exception) { fileCb = null; false }
            }
            // 🎤 녹음: 안드로이드 마이크 권한을 받은 뒤 화면에 허용
            override fun onPermissionRequest(request: PermissionRequest) {
                runOnUiThread {
                    if (!request.resources.contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) { request.deny(); return@runOnUiThread }
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        request.grant(arrayOf(PermissionRequest.RESOURCE_AUDIO_CAPTURE))
                    } else {
                        micReq = request
                        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 42)
                    }
                }
            }
        }
        w.webViewClient = object : WebViewClient() {
            private fun fallback(view: WebView, req: WebResourceRequest) {
                // 인터넷이 안 되거나 페이지가 아직 없으면 앱 안에 넣어 둔 사본
                if (req.isForMainFrame && !req.url.toString().startsWith("file:")) {
                    view.loadUrl("file:///android_asset/$page")
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
        // 켤 때마다 최신 화면 (주소 뒤에 시각)
        val path = page.substringBefore("#")
        val hash = if (page.contains("#")) "#" + page.substringAfter("#") else ""
        w.loadUrl(Study.PAGES + path + "?t=" + System.currentTimeMillis() + hash)
        return w
    }

    private fun select(i: Int) {
        if (i !in tabs.indices) return
        cur = i
        webs.forEachIndexed { k, w -> w.visibility = if (k == i) View.VISIBLE else View.GONE }
        buttons.forEachIndexed { k, t ->
            t.setTextColor(if (k == i) Color.WHITE else Color.parseColor("#8A8FA0"))
            t.typeface = if (k == i) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
        getSharedPreferences("study", MODE_PRIVATE).edit().putString("tabKey", tabs[i].key).apply()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != 41) return
        val uri = if (resultCode == RESULT_OK) data?.data else null
        fileCb?.onReceiveValue(if (uri != null) arrayOf(uri) else null)
        fileCb = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 42) return
        val r = micReq ?: return
        micReq = null
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) r.grant(arrayOf(PermissionRequest.RESOURCE_AUDIO_CAPTURE))
        else r.deny()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val key = intent.getStringExtra("tab") ?: return
        val i = tabs.indexOfFirst { t -> t.key == key }
        if (i >= 0) {
            select(i)
            webs[i].evaluateJavascript("try{pull()}catch(e){}", null)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Me.role(this) == "0003") AlarmReceiver.scheduleAll(this)   // 시간표가 바뀌었을 수 있음
        webs.forEach { w ->
            w.onResume()
            // 다른 휴대폰에서 바꾼 내용 바로 불러오기
            w.evaluateJavascript("try{pull()}catch(e){};try{gasPull()}catch(e){}", null)
        }
    }

    override fun onPause() {
        super.onPause()
        if (Me.role(this) == "0000") TodayWidget.refresh(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val w = webs.getOrNull(cur)
        if (w != null && w.canGoBack()) w.goBack() else super.onBackPressed()
    }

    /** 화면(웹페이지)에서 부르는 앱 기능 */
    inner class Bridge {
        @JavascriptInterface fun role(): String = Me.role(this@MainActivity)
        @JavascriptInterface fun pw(): String = Me.pw(this@MainActivity)
        @JavascriptInterface fun prefs(): String = Me.prefsJson(this@MainActivity)
        @JavascriptInterface fun version(): String = BuildConfig.VERSION_CODE.toString()
        @JavascriptInterface fun setPref(key: String, on: Boolean) {
            Me.setOn(this@MainActivity, key, on)
            AlarmReceiver.scheduleAll(this@MainActivity)
        }
        @JavascriptInterface fun resetRole() {
            runOnUiThread {
                Me.clear(this@MainActivity)
                AlarmReceiver.scheduleAll(this@MainActivity)
                webs.forEach { w -> w.destroy() }
                webs.clear()
                showLogin()
            }
        }
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
                val notes = j.optString("body", "").trim()
                if (latest > BuildConfig.VERSION_CODE && apk != null) {
                    runOnUiThread {
                        if (!isFinishing) {
                            AlertDialog.Builder(this)
                                .setTitle("새 버전이 있습니다")
                                .setMessage("지금 v${BuildConfig.VERSION_CODE} → 새 버전 v$latest" +
                                    (if (notes.isNotEmpty()) "\n\n[바뀐 점]\n$notes" else "") +
                                    "\n\n'업데이트'를 누르면 새 APK를 내려받습니다. 다 받은 뒤 눌러서 설치하면 기록과 설정은 그대로 유지됩니다.")
                                .setPositiveButton("업데이트") { _, _ ->
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(apk)))
                                }
                                .setNegativeButton("나중에", null)
                                .show()
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }.start()
    }
}
