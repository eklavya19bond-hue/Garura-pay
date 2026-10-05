package com.garurapay.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlin.random.Random

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("garura_session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "active_user_id"
        private const val KEY_USER_EMAIL = "active_user_email"
        private const val KEY_USER_NAME = "active_user_name"
        private const val KEY_USER_BALANCE = "active_user_balance"
    }

    /**
     * Generates a unique 9-digit User ID (range 100,000,000 to 999,999,999)
     */
    fun generate9DigitUserId(): String {
        return Random.nextLong(100_000_000L, 1_000_000_000L).toString()
    }

    fun saveUserSession(userId: String, email: String, name: String, balance: Double) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
            putFloat(KEY_USER_BALANCE, balance.toFloat())
            apply()
        }
    }

    fun getUserId(): String = prefs.getString(KEY_USER_ID, "839201948") ?: "839201948"
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "Garura Trader") ?: "Garura Trader"
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "trader@garurapay.com") ?: "trader@garurapay.com"
    fun getUserBalance(): Double = prefs.getFloat(KEY_USER_BALANCE, 0.00f).toDouble()

    fun updateBalance(newBalance: Double) {
        prefs.edit().putFloat(KEY_USER_BALANCE, newBalance.toFloat()).apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
