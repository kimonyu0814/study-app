package com.onyu.study

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.ZonedDateTime

/** 정해진 시각 알림. 사람마다 다른 알림이 울림 */
class AlarmReceiver : BroadcastReceiver() {

    /** 종류, 시, 분, 평일만, 설정 이름, 받는 사람 */
    class Kind(val key: String, val h: Int, val m: Int, val weekdays: Boolean, val pref: String, val role: String)

    override fun onReceive(ctx: Context, intent: Intent) {
        if (intent.action != ACTION_ALARM) {
            scheduleAll(ctx)          // 휴대폰 재시작 · 앱 업데이트 후 다시 예약
            return
        }
        val kind = intent.getStringExtra("kind") ?: ""
        val pending = goAsync()
        Thread {
            try {
                fire(ctx, kind)
            } catch (e: Exception) {
            } finally {
                scheduleAll(ctx)
                pending.finish()
            }
        }.start()
    }

    private fun fire(ctx: Context, kind: String) {
        when (kind) {
            "am", "pm" -> {
                val s = Study.summarize(Study.load(ctx))
                Notify.study(ctx, kind, s)
                TodayWidget.updateAll(ctx, s)
            }
            "ask" -> Notify.simple(ctx, 3, "오늘 저녁 · 데리러 올 곳",
                listOf("저녁을 먹는지, 어디로 데리러 오면 되는지 알려 주세요. 누르면 바로 답할 수 있습니다."), "admin")
            "pickup" -> {
                val t = Family.pickupText(Family.load())
                Notify.simple(ctx, 4, t.first, listOf(t.second), "schedule")
            }
        }
    }

    companion object {
        const val ACTION_ALARM = "com.onyu.study.ALARM"
        val KINDS = listOf(
            Kind("am", 8, 0, false, "n_am", "0000"),
            Kind("pm", 17, 50, false, "n_pm", "0000"),
            Kind("ask", 8, 50, true, "n_ask", "0000"),
            Kind("pickup", 21, 0, true, "n_pickup", "0002")
        )

        /** 이 휴대폰 주인과 설정에 맞게 모든 알림을 다시 예약 */
        fun scheduleAll(ctx: Context) {
            val role = Me.role(ctx)
            val am = ctx.getSystemService(AlarmManager::class.java)
            KINDS.forEachIndexed { i, k ->
                val intent = Intent(ctx, AlarmReceiver::class.java).setAction(ACTION_ALARM).putExtra("kind", k.key)
                val pi = PendingIntent.getBroadcast(ctx, 10 + i, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                if (role != k.role || !Me.on(ctx, k.pref)) {
                    am.cancel(pi)
                    return@forEachIndexed
                }
                val at = next(k).toInstant().toEpochMilli()
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

        private fun next(k: Kind): ZonedDateTime {
            val now = ZonedDateTime.now(Study.KST)
            for (day in 0L..8L) {
                val t = now.toLocalDate().plusDays(day).atTime(k.h, k.m).atZone(Study.KST)
                if (!t.isAfter(now.plusSeconds(30))) continue
                if (k.weekdays && t.dayOfWeek.value >= 6) continue
                return t
            }
            return now.plusDays(1)
        }
    }
}
