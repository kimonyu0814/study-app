package com.onyu.study

import android.content.Context
import org.json.JSONObject

/** 이 휴대폰의 주인 (식별 코드) 과 알림 설정 */
object Me {
    val PEOPLE = mapOf("0000" to "온유", "0001" to "엄마", "0002" to "아빠", "0003" to "태은")

    /** 사람마다 켜고 끌 수 있는 알림 */
    val PREFS = mapOf(
        "0000" to listOf("n_am", "n_pm", "n_ask", "n_msg"),
        "0001" to listOf("n_dinner", "n_home", "n_msg"),
        "0002" to listOf("n_pickup", "n_home", "n_msg"),
        "0003" to listOf("n_class", "n_study", "n_done", "n_msg")
    )

    private fun sp(ctx: Context) = ctx.getSharedPreferences("me", Context.MODE_PRIVATE)

    fun role(ctx: Context): String = sp(ctx).getString("role", "") ?: ""
    fun pw(ctx: Context): String = sp(ctx).getString("pw", "") ?: ""

    fun set(ctx: Context, role: String, pw: String) {
        sp(ctx).edit().putString("role", role).putString("pw", pw).apply()
    }

    fun clear(ctx: Context) {
        sp(ctx).edit().clear().apply()
    }

    fun on(ctx: Context, key: String): Boolean = sp(ctx).getBoolean(key, true)

    fun setOn(ctx: Context, key: String, value: Boolean) {
        sp(ctx).edit().putBoolean(key, value).apply()
    }

    fun prefsJson(ctx: Context): String {
        val o = JSONObject()
        (PREFS[role(ctx)] ?: emptyList()).forEach { k -> o.put(k, on(ctx, k)) }
        return o.toString()
    }
}
