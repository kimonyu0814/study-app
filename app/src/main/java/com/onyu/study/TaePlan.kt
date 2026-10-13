package com.onyu.study

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

/** 태은이 시간표로 알림 만들기: 학원 10분 전 · 집공부 시작 · 오후 9시 끝 */
object TaePlan {
    class Ev(val d: Int, val s: String, val e: String, val t: String)
    class Note(val at: ZonedDateTime, val title: String, val body: String, val pref: String)

    /** 서버에 아직 태은이 시간표가 없을 때 쓰는 기본값 (family.html 과 같음) */
    private val DEFAULT = listOf(
        Ev(0, "15:00", "", "귀가 (방과후 줄넘기 후)"), Ev(0, "15:30", "17:00", "미술"), Ev(0, "17:30", "19:00", "영어"),
        Ev(0, "19:00", "19:40", "🍚 저녁"), Ev(0, "19:40", "21:00", "집공부 (국어, 수학)"),
        Ev(1, "14:30", "", "귀가"), Ev(1, "15:00", "15:50", "피아노"), Ev(1, "16:00", "18:00", "집공부"),
        Ev(1, "18:00", "18:40", "🍚 저녁"), Ev(1, "18:40", "21:00", "집공부 (영숙이, 국어, 수학)"),
        Ev(2, "13:50", "", "귀가"), Ev(2, "14:30", "16:00", "영어"), Ev(2, "16:00", "17:30", "빈 시간 (집공부 가능)"),
        Ev(2, "17:30", "19:00", "한우리"), Ev(2, "19:00", "19:40", "🍚 저녁"), Ev(2, "19:40", "21:00", "집공부 (영숙이, 수학)"),
        Ev(3, "13:50", "", "귀가"), Ev(3, "15:00", "16:30", "영어"), Ev(3, "17:00", "17:50", "피아노"),
        Ev(3, "18:00", "18:40", "🍚 저녁"), Ev(3, "18:40", "21:00", "집공부 (국어, 수학)"),
        Ev(4, "13:50", "", "귀가"), Ev(4, "15:10", "16:30", "생명과학"), Ev(4, "16:30", "17:30", "집공부"),
        Ev(4, "17:30", "18:10", "🍚 저녁"), Ev(4, "18:30", "19:30", "축구"), Ev(4, "19:40", "21:00", "집공부 (영숙이, 국어, 수학)")
    )

    private fun parseEv(o: JSONObject): Ev = Ev(o.optInt("d", -1), o.optString("s", ""), o.optString("e", ""), o.optString("t", ""))

    fun events(data: JSONObject?, day: LocalDate): List<Ev> {
        val di = day.dayOfWeek.value - 1
        val t = data?.optJSONObject("schedule")?.optJSONObject("taeeun") ?: return DEFAULT.filter { x -> x.d == di }
        val monday = day.minusDays(di.toLong()).toString()
        val wk = t.optJSONObject("weeks")?.optJSONObject(monday)
        val skip = HashSet<String>()
        wk?.optJSONArray("skip")?.let { a -> for (i in 0 until a.length()) skip.add(a.optString(i)) }
        val out = ArrayList<Ev>()
        t.optJSONArray("fixed")?.let { a ->
            for (i in 0 until a.length()) {
                val o = a.optJSONObject(i) ?: continue
                if (o.optInt("d", -1) == di && !skip.contains(o.optString("id"))) out.add(parseEv(o))
            }
        }
        wk?.optJSONArray("add")?.let { a ->
            for (i in 0 until a.length()) {
                val o = a.optJSONObject(i) ?: continue
                if (o.optInt("d", -1) == di) out.add(parseEv(o))
            }
        }
        return out
    }

    /** 태은이용 12시간제: "15:00" → "오후 3:00" */
    fun kid(hm: String): String = try {
        val t = LocalTime.parse(hm)
        (if (t.hour < 12) "오전 " else "오후 ") + (if (t.hour % 12 == 0) 12 else t.hour % 12) + ":" + String.format("%02d", t.minute)
    } catch (e: Exception) { hm }

    fun kidRange(s: String, e: String): String {
        val a = kid(s)
        if (e.isEmpty()) return a
        val b = kid(e)
        return if (a.take(2) == b.take(2)) a + "–" + b.drop(3) else "$a–$b"
    }

    private fun time(day: LocalDate, hm: String): ZonedDateTime? = try {
        day.atTime(LocalTime.parse(hm)).atZone(Study.KST)
    } catch (e: Exception) { null }

    /** from 이후 7일 동안의 알림 목록 */
    fun notes(data: JSONObject?, from: ZonedDateTime): List<Note> {
        val out = ArrayList<Note>()
        for (k in 0L..7L) {
            val day = from.toLocalDate().plusDays(k)
            val evs = events(data, day)
            var study = false
            for (e in evs) {
                val start = time(day, e.s) ?: continue
                val span = kidRange(e.s, e.e)
                when {
                    e.t.contains("집공부") -> {
                        study = true
                        val subj = Regex("\\((.*)\\)").find(e.t)?.groupValues?.get(1)
                        out.add(Note(start, "집공부 시간이에요 📚", if (subj != null) "오늘 할 것: $subj ($span)" else "공부를 시작해요 ($span)", "n_study"))
                    }
                    e.t.contains("귀가") || e.t.contains("저녁") || e.t.contains("빈 시간") -> { }
                    else -> out.add(Note(start.minusMinutes(10), "10분 뒤 ${e.t}", "$span · 준비물 챙겨요!", "n_class"))
                }
            }
            if (study && day.dayOfWeek.value <= 5) {
                time(day, "21:00")?.let { t -> out.add(Note(t, "오늘 공부 끝! 🎉", "수고했어요. 푹 쉬어요.", "n_done")) }
            }
        }
        return out.sortedBy { n -> n.at }
    }

    private fun pi(ctx: Context, i: Intent?): PendingIntent {
        val intent = i ?: Intent(ctx, AlarmReceiver::class.java).setAction(AlarmReceiver.ACTION_ALARM).putExtra("kind", "tae")
        return PendingIntent.getBroadcast(ctx, 30, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    fun cancel(ctx: Context) {
        ctx.getSystemService(AlarmManager::class.java).cancel(pi(ctx, null))
    }

    /** 시간표를 받아(안 되면 저장된 것) 다음 알림 하나를 예약. 네트워크를 쓰니 백그라운드에서 부를 것 */
    fun scheduleNext(ctx: Context) {
        val sp = ctx.getSharedPreferences("study", Context.MODE_PRIVATE)
        var data = Family.load()
        if (data != null) sp.edit().putString("familyData", data.toString()).apply()
        else data = try { sp.getString("familyData", null)?.let { s -> JSONObject(s) } } catch (e: Exception) { null }
        val now = ZonedDateTime.now(Study.KST)
        val next = notes(data, now).firstOrNull { n -> n.at.isAfter(now.plusSeconds(30)) && Me.on(ctx, n.pref) }
        if (next == null) { cancel(ctx); return }
        val intent = Intent(ctx, AlarmReceiver::class.java).setAction(AlarmReceiver.ACTION_ALARM)
            .putExtra("kind", "tae").putExtra("title", next.title).putExtra("body", next.body).putExtra("pref", next.pref)
        val p = pi(ctx, intent)
        val am = ctx.getSystemService(AlarmManager::class.java)
        val at = next.at.toInstant().toEpochMilli()
        try {
            if (Build.VERSION.SDK_INT >= 31 && !am.canScheduleExactAlarms()) am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, p)
            else am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, p)
        } catch (e: SecurityException) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, p)
        }
    }
}
