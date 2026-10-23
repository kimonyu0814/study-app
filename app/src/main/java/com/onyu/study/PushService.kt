package com.onyu.study

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/** 서버에서 오는 푸시 (메시지 · 저녁 알림) */
class PushService : FirebaseMessagingService() {

    override fun onMessageReceived(msg: RemoteMessage) {
        val d = msg.data
        MainActivity.live?.refreshNow()      // 앱이 켜져 있으면 화면도 바로 새로
        val title = d["title"] ?: msg.notification?.title ?: return
        val body = d["body"] ?: msg.notification?.body ?: ""
        // 알림 종류별로 켜고 끔 (밥 · 데리러 · 귀가 · 메시지)
        val kind = d["kind"] ?: "msg"
        val key = "n_$kind"
        val prefs = Me.PREFS[Me.role(this)] ?: emptyList()
        if (!Me.on(this, if (prefs.contains(key)) key else "n_msg")) return
        Notify.simple(this, (System.currentTimeMillis() % 100000).toInt() + 100, title, listOf(body), d["tab"] ?: "", Notify.CH_MSG)
    }

    override fun onNewToken(token: String) {
        Family.register(this, token)
    }
}
