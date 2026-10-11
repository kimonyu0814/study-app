package com.onyu.study

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

/** 알림 만들기 */
object Notify {
    const val CH = "study"
    const val CH_MSG = "family"

    fun channel(ctx: Context) {
        val nm = ctx.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(NotificationChannel(CH, "할 일 · 정해진 시각 알림", NotificationManager.IMPORTANCE_DEFAULT))
        nm.createNotificationChannel(NotificationChannel(CH_MSG, "가족 메시지", NotificationManager.IMPORTANCE_HIGH))
    }

    /** 제목 + 여러 줄 알림. tab 은 눌렀을 때 열 탭 */
    fun simple(ctx: Context, id: Int, title: String, lines: List<String>, tab: String, ch: String = CH) {
        if (Build.VERSION.SDK_INT >= 33 &&
            ctx.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        channel(ctx)
        val open = Intent(ctx, MainActivity::class.java).putExtra("tab", tab)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pi = PendingIntent.getActivity(ctx, id, open, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val b = Notification.Builder(ctx, ch)
            .setSmallIcon(R.drawable.ic_stat)
            .setContentTitle(title)
            .setContentIntent(pi)
            .setAutoCancel(true)
        val text = lines.firstOrNull() ?: ""
        b.setContentText(text)
        if (lines.size > 1) {
            val st = Notification.InboxStyle()
            lines.forEach { l -> st.addLine(l) }
            b.setStyle(st)
        } else {
            b.setStyle(Notification.BigTextStyle().bigText(text))
        }
        ctx.getSystemService(NotificationManager::class.java).notify(id, b.build())
    }

    /** 아침 · 저녁 할 일 알림 (온유) */
    fun study(ctx: Context, kind: String, s: Study.Summary) {
        val lines = ArrayList<String>()
        val doneToday = s.today.count { x -> x.done }
        val title: String
        if (kind == "am") {
            title = "오늘 할 일 ${s.today.size}개" +
                (if (s.late.isNotEmpty()) " · 밀린 일 ${s.late.size}개" else "") +
                (if (s.dday != null) " · ${s.dday}" else "")
            s.today.forEach { x -> lines.add(Study.line(x)) }
            if (s.today.isEmpty()) lines.add("오늘 잡힌 할 일이 없습니다.")
        } else {
            val left = s.today.size - doneToday
            title = "오늘 $doneToday/${s.today.size} 완료" + (if (left > 0) " · 남은 일 ${left}개" else "")
            s.today.filter { x -> !x.done }.forEach { x -> lines.add(Study.line(x)) }
            if (s.today.isNotEmpty() && left == 0) lines.add("오늘 할 일을 모두 끝냈습니다!")
            if (s.tomorrow.isNotEmpty()) lines.add("내일: " + s.tomorrow.joinToString(", ") { x -> x.text })
            if (lines.isEmpty()) lines.add("오늘 잡힌 할 일이 없습니다.")
        }
        s.late.take(3).forEach { x -> lines.add("밀림 ${Study.label(x.date)} ${x.subject} · ${x.text}") }
        if (s.all > 0) lines.add("전체 계획 ${s.allDone}/${s.all} 완료")
        simple(ctx, if (kind == "am") 1 else 2, title, lines, "study")
    }
}
