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

/** 아침 · 저녁 할 일 알림 */
object Notify {
    private const val CH = "study"

    fun channel(ctx: Context) {
        val nm = ctx.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(NotificationChannel(CH, "할 일 알림", NotificationManager.IMPORTANCE_DEFAULT))
    }

    fun show(ctx: Context, kind: String, s: Study.Summary) {
        if (Build.VERSION.SDK_INT >= 33 &&
            ctx.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        channel(ctx)
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

        val open = PendingIntent.getActivity(
            ctx, 0, Intent(ctx, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val style = Notification.InboxStyle()
        lines.forEach { l -> style.addLine(l) }
        val n = Notification.Builder(ctx, CH)
            .setSmallIcon(R.drawable.ic_stat)
            .setContentTitle(title)
            .setContentText(lines.first())
            .setStyle(style)
            .setContentIntent(open)
            .setAutoCancel(true)
            .build()
        ctx.getSystemService(NotificationManager::class.java).notify(if (kind == "am") 1 else 2, n)
    }
}
