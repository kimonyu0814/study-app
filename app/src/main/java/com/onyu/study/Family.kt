package com.onyu.study

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

/** 가족 데이터 (스케줄 · 오늘 답 · 메시지) 서버와 주고받기 */
object Family {

    fun post(body: JSONObject): JSONObject {
        val c = URL(Study.API).openConnection() as HttpURLConnection
        c.connectTimeout = 15000
        c.readTimeout = 20000
        c.requestMethod = "POST"
        c.doOutput = true
        c.instanceFollowRedirects = true
        c.setRequestProperty("Content-Type", "text/plain;charset=utf-8")
        try {
            c.outputStream.use { o -> o.write(body.toString().toByteArray(Charsets.UTF_8)) }
            var code = c.responseCode
            var conn = c
            // Apps Script 는 결과를 다른 주소로 넘겨 줌 (POST 뒤 302 → GET)
            var hops = 0
            while ((code == 301 || code == 302 || code == 303) && hops < 3) {
                val loc = conn.getHeaderField("Location") ?: break
                conn = URL(loc).openConnection() as HttpURLConnection
                conn.connectTimeout = 15000
                conn.readTimeout = 20000
                code = conn.responseCode
                hops++
            }
            if (code !in 200..299) throw IOException("HTTP $code")
            return JSONObject(conn.inputStream.bufferedReader().use { r -> r.readText() })
        } finally {
            c.disconnect()
        }
    }

    /** 0000 비밀번호 확인 (인터넷 필요) */
    fun auth(pw: String): Boolean {
        val j = post(JSONObject().put("app", "family").put("action", "auth").put("from", "0000").put("pw", pw))
        return j.optBoolean("ok", false)
    }

    /** 이 휴대폰으로 푸시 알림을 받도록 서버에 등록 */
    fun register(ctx: Context, token: String? = null) {
        val app = ctx.applicationContext
        val role = Me.role(app)
        if (role.isEmpty()) return
        val send = { t: String ->
            Thread {
                try {
                    post(JSONObject().put("app", "family").put("action", "register")
                        .put("from", role).put("pw", Me.pw(app)).put("token", t))
                } catch (e: Exception) { }
            }.start()
        }
        if (token != null) { send(token); return }
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) task.result?.let { t -> send(t) }
            }
        } catch (e: Exception) { }
    }

    fun load(): JSONObject? = try {
        JSONObject(Study.httpGet(Study.API + "?app=family&t=" + System.currentTimeMillis())).optJSONObject("data")
    } catch (e: Exception) { null }

    /** 아빠 오후 9시 알림 내용 */
    fun pickupText(data: JSONObject?): Pair<String, String> {
        val today = LocalDate.now(Study.KST)
        val k = today.toString()
        val a = data?.optJSONObject("days")?.optJSONObject(k)
        val p = a?.optString("pickup", "") ?: ""
        if (p == "필요 없음") return "오늘은 데리러 오지 않아도 돼요" to "온유가 '필요 없음'으로 답했습니다."
        if (p.isNotEmpty()) return "오늘 데리러 갈 곳: $p" to "온유가 알려 준 곳입니다. (${Study.label(k)})"
        // 답이 없으면 오늘 스케줄의 마지막 일정을 알려 줌
        val last = lastEvent(data, today)
        return if (last != null) "아직 답이 없어요" to "오늘 마지막 일정: $last"
        else "아직 답이 없어요" to "메시지 탭에서 온유에게 물어보세요."
    }

    private fun lastEvent(data: JSONObject?, day: LocalDate): String? {
        val sch = data?.optJSONObject("schedule") ?: return null
        val di = day.dayOfWeek.value - 1
        val monday = day.minusDays(di.toLong()).toString()
        val wk = sch.optJSONObject("weeks")?.optJSONObject(monday)
        val skip = HashSet<String>()
        wk?.optJSONArray("skip")?.let { a -> for (i in 0 until a.length()) skip.add(a.optString(i)) }
        val evs = ArrayList<JSONObject>()
        fun take(a: JSONArray?, checkSkip: Boolean) {
            if (a == null) return
            for (i in 0 until a.length()) {
                val e = a.optJSONObject(i) ?: continue
                if (e.optInt("d", -1) != di) continue
                if (checkSkip && skip.contains(e.optString("id"))) continue
                evs.add(e)
            }
        }
        take(sch.optJSONArray("fixed"), true)
        take(wk?.optJSONArray("add"), false)
        val e = evs.maxByOrNull { x -> x.optString("e", x.optString("s", "")) } ?: return null
        val place = e.optString("p", "")
        val end = e.optString("e", "")
        return e.optString("t", "") + (if (end.isNotEmpty()) " ($end 끝" + (if (place.isNotEmpty()) ", $place)" else ")") else if (place.isNotEmpty()) " ($place)" else "")
    }
}
