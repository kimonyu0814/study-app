package com.onyu.study

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/** 홈 화면 위젯: D-day + 오늘 할 일 */
class TodayWidget : AppWidgetProvider() {

    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        val pending = goAsync()
        Thread {
            try {
                updateAll(ctx, Study.summarize(Study.load(ctx)))
            } catch (e: Exception) {
            } finally {
                pending.finish()
            }
        }.start()
    }

    companion object {
        fun updateAll(ctx: Context, s: Study.Summary) {
            val mgr = AppWidgetManager.getInstance(ctx)
            val ids = mgr.getAppWidgetIds(ComponentName(ctx, TodayWidget::class.java))
            if (ids.isEmpty()) return
            val v = RemoteViews(ctx.packageName, R.layout.widget_today)
            v.setTextViewText(R.id.w_dday, s.dday ?: "D-?")
            val done = s.today.count { x -> x.done }
            v.setTextViewText(R.id.w_head, "오늘 할 일 $done/${s.today.size}")
            val sb = StringBuilder()
            if (s.today.isEmpty()) sb.append("오늘 잡힌 할 일이 없습니다.")
            else sb.append(s.today.joinToString("\n") { x -> Study.line(x) })
            if (s.late.isNotEmpty()) sb.append("\n밀린 일 ${s.late.size}개")
            v.setTextViewText(R.id.w_list, sb.toString())
            val open = PendingIntent.getActivity(
                ctx, 0, Intent(ctx, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            v.setOnClickPendingIntent(R.id.w_root, open)
            mgr.updateAppWidget(ids, v)
        }

        /** 앱에서 체크한 뒤 위젯 새로 고침 */
        fun refresh(ctx: Context) {
            val app = ctx.applicationContext
            Thread {
                try { updateAll(app, Study.summarize(Study.load(app))) } catch (e: Exception) { }
            }.start()
        }
    }
}
