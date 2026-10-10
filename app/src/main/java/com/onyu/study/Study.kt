package com.onyu.study

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/** 학습기록 데이터(구글 시트) 읽기 + 오늘 할 일 정리 */
object Study {
    const val API = "https://script.google.com/macros/s/AKfycbzwUGDOfoBCJbPQx96HCMN8vin0OPCcREB-kYGv-yiDVGkHIOlapcL85oasQRA71NdX/exec"
    const val PAGES = "https://kimonyu0814.github.io/study-app/"
    const val REPO = "kimonyu0814/study-app"
    val KST: ZoneId = ZoneId.of("Asia/Seoul")
    private val WD = arrayOf("월", "화", "수", "목", "금", "토", "일")

    class Item(val date: String, val text: String, val done: Boolean, val subject: String)

    class Summary(
        val today: List<Item>,
        val tomorrow: List<Item>,
        val late: List<Item>,
        val dday: String?,
        val allDone: Int,
        val all: Int
    )

    fun httpGet(url: String, timeoutMs: Int = 15000): String {
        val c = URL(url).openConnection() as HttpURLConnection
        c.connectTimeout = timeoutMs
        c.readTimeout = timeoutMs
        c.instanceFollowRedirects = true
        c.setRequestProperty("User-Agent", "study-app")
        try {
            val code = c.responseCode
            if (code !in 200..299) throw IOException("HTTP $code")
            return c.inputStream.bufferedReader().use { r -> r.readText() }
        } finally {
            c.disconnect()
        }
    }

    /** 서버에서 받고, 실패하면 마지막으로 받은 내용을 씀 */
    fun load(ctx: Context): JSONObject? {
        val sp = ctx.getSharedPreferences("study", Context.MODE_PRIVATE)
        try {
            val j = JSONObject(httpGet(API + "?app=exam&t=" + System.currentTimeMillis()))
            val d = j.optJSONObject("data")
            if (d != null) {
                sp.edit().putString("data", d.toString()).apply()
                return d
            }
        } catch (e: Exception) {
            // 인터넷이 안 되면 저장된 내용 사용
        }
        val cached = sp.getString("data", null) ?: return null
        return try { JSONObject(cached) } catch (e: Exception) { null }
    }

    fun label(ymd: String): String {
        return try {
            val d = LocalDate.parse(ymd)
            "${d.monthValue}/${d.dayOfMonth}(${WD[d.dayOfWeek.value - 1]})"
        } catch (e: Exception) { ymd }
    }

    fun line(i: Item): String = (if (i.done) "✓ " else "□ ") + i.subject + " · " + i.text

    fun summarize(data: JSONObject?): Summary {
        val today = LocalDate.now(KST)
        val t = today.toString()
        val tm = today.plusDays(1).toString()
        val items = ArrayList<Item>()
        var dday: String? = null
        if (data != null) {
            val termKey = data.optString("curTerm", "1-2")
            val term = data.optJSONObject("terms")?.optJSONObject(termKey) ?: JSONObject()
            val list = term.optJSONArray("subjList") ?: JSONArray()
            val subs = term.optJSONObject("subjects") ?: JSONObject()
            for (i in 0 until list.length()) {
                val sj = list.optJSONObject(i) ?: continue
                val pair = subs.optJSONObject(sj.optString("k")) ?: continue
                for (ex in arrayOf("mid", "fin")) {
                    val o = pair.optJSONObject(ex) ?: continue
                    collect(items, o.optJSONArray("todo"), "date", sj.optString("n"), "")
                    collect(items, o.optJSONArray("hw"), "due", sj.optString("n"), "[학원 숙제] ")
                }
            }
            collect(items, data.optJSONArray("cal"), "date", "공통", "")
            val ed = term.optJSONObject("examDates")
            if (ed != null) {
                for (pr in listOf("mid" to "중간", "fin" to "기말")) {
                    if (dday != null) break
                    val d = (try { LocalDate.parse(ed.optString(pr.first, "")) } catch (e: Exception) { null }) ?: continue
                    val diff = ChronoUnit.DAYS.between(today, d)
                    if (diff >= 0) dday = pr.second + " " + (if (diff == 0L) "D-day" else "D-$diff")
                }
            }
        }
        items.sortBy { x -> x.date }
        return Summary(
            today = items.filter { x -> x.date == t },
            tomorrow = items.filter { x -> x.date == tm },
            late = items.filter { x -> x.date < t && !x.done },
            dday = dday,
            allDone = items.count { x -> x.done },
            all = items.size
        )
    }

    private fun collect(out: MutableList<Item>, arr: JSONArray?, key: String, subject: String, prefix: String) {
        if (arr == null) return
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val d = o.optString(key, "")
            val tx = o.optString("t", "")
            if (d.isEmpty() || tx.isEmpty()) continue
            out.add(Item(d, prefix + tx, o.optBoolean("done", false), subject))
        }
    }
}
