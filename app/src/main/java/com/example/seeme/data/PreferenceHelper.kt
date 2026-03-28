package com.example.seeme.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "SeemePrefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_FCM_TOKEN = "fcm_token"

        @Volatile
        private var instance: PreferenceHelper? = null

        fun getInstance(context: Context): PreferenceHelper {
            return instance ?: synchronized(this) {
                instance ?: PreferenceHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    var userId: String?
        get() = sharedPref.getString(KEY_USER_ID, null)
        set(value) = sharedPref.edit().putString(KEY_USER_ID, value).apply()

    var userEmail: String?
        get() = sharedPref.getString(KEY_USER_EMAIL, null)
        set(value) = sharedPref.edit().putString(KEY_USER_EMAIL, value).apply()

    var userName: String?
        get() = sharedPref.getString(KEY_USER_NAME, null)
        set(value) = sharedPref.edit().putString(KEY_USER_NAME, value).apply()

    var fcmToken: String?
        get() = sharedPref.getString(KEY_FCM_TOKEN, null)
        set(value) = sharedPref.edit().putString(KEY_FCM_TOKEN, value).apply()

    fun clear() {
        sharedPref.edit().clear().apply()
    }
}
