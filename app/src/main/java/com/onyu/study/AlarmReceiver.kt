package com.onyu.study

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.ZonedDateTime

/** 정해진 시각에 알림을 보내고 다음 알림을 예약 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(ctx: Context, intent: Intent) {
        if (intent.action != ACTION_ALARM) {
            schedule(ctx)            // 휴대폰 재시작 · 앱 업데이트 후
            return
        }
        val kind = intent.getStringExtra("kind") ?: "am"
        val pending = goAsync()
        Thread {
            try {
                val s = Study.summarize(Study.load(ctx))
                Notify.show(ctx, kind, s)
                TodayWidget.updateAll(ctx, s)
            } catch (e: Exception) {
                // 다음 알림은 그대로 예약
            } finally {
                schedule(ctx)
                pending.finish()
            }
        }.start()
    }

    companion object {
        const val ACTION_ALARM = "com.onyu.study.ALARM"
        /** 알림 시각 (시, 분) — 아침 8:00, 저녁 5:50 */
        private val TIMES = listOf(8 to 0, 17 to 50)

        fun schedule(ctx: Context) {
            val now = ZonedDateTime.now(Study.KST)
            var best: ZonedDateTime? = null
            var kind = "am"
            for (day in 0L..1L) {
                for (hm in TIMES) {
                    val t = now.toLocalDate().plusDays(day).atTime(hm.first, hm.second).atZone(Study.KST)
                    val b = best
                    if (t.isAfter(now.plusSeconds(30)) && (b == null || t.isBefore(b))) {
                        best = t
                        kind = if (hm.first < 12) "am" else "pm"
                    }
                }
            }
            val at = (best ?: now.plusHours(12)).toInstant().toEpochMilli()
            val i = Intent(ctx, AlarmReceiver::class.java).setAction(ACTION_ALARM).putExtra("kind", kind)
            val pi = PendingIntent.getBroadcast(
                ctx, 1, i, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val am = ctx.getSystemService(AlarmManager::class.java)
            try {
                if (Build.VERSION.SDK_INT >= 31 && !am.canScheduleExactAlarms()) {
                    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi)
                } else {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi)
                }
            } catch (e: SecurityException) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi)
            }
        }
    }
}
