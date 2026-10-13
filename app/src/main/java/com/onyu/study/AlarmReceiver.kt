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
    private var intentTitle: String? = null
    private var intentBody: String? = null
    private var intentPref: String? = null

    /** 종류, 시, 분, 평일만, 설정 이름, 받는 사람 */
    class Kind(val key: String, val h: Int, val m: Int, val weekdays: Boolean, val pref: String, val role: String)

    override fun onReceive(ctx: Context, intent: Intent) {
        if (intent.action != ACTION_ALARM) {
            // 휴대폰 재시작 · 앱 업데이트 후 다시 예약
            val p = goAsync()
            Thread {
                try {
                    scheduleAll(ctx)
                    if (Me.role(ctx) == "0003") TaePlan.scheduleNext(ctx)
                } catch (e: Exception) {
                } finally { p.finish() }
            }.start()
            return
        }
        val kind = intent.getStringExtra("kind") ?: ""
        intentTitle = intent.getStringExtra("title")
        intentBody = intent.getStringExtra("body")
        intentPref = intent.getStringExtra("pref")
        val pending = goAsync()
        Thread {
            try {
                fire(ctx, kind)
            } catch (e: Exception) {
            } finally {
                try {
                    scheduleAll(ctx)
                    if (Me.role(ctx) == "0003") TaePlan.scheduleNext(ctx)
                } catch (e: Exception) { }
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
            "tae" -> {
                val title = intentTitle ?: return
                if (Me.on(ctx, intentPref ?: "n_class")) Notify.simple(ctx, 5, title, listOf(intentBody ?: ""), "schedule")
            }
            "ask" -> Notify.simple(ctx, 3, "밥 · 데리러 올 곳",
                listOf("오후 9시에 엄마(밥) · 아빠(데리러)에게 각각 갑니다. 누르면 바로 답할 수 있습니다."), "admin")
        }
    }

    companion object {
        const val ACTION_ALARM = "com.onyu.study.ALARM"
        val KINDS = listOf(
            Kind("am", 8, 0, false, "n_am", "0000"),
            Kind("pm", 17, 50, false, "n_pm", "0000"),
            Kind("ask", 20, 50, true, "n_ask", "0000")
        )

        /** 이 휴대폰 주인과 설정에 맞게 모든 알림을 다시 예약 */
        fun scheduleAll(ctx: Context) {
            val role = Me.role(ctx)
            // 태은: 시간표에 따라 다음 알림 하나씩 (인터넷에서 시간표를 받아야 해서 따로)
            if (role == "0003") Thread { try { TaePlan.scheduleNext(ctx) } catch (e: Exception) { } }.start()
            else TaePlan.cancel(ctx)
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
