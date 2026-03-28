package com.example.seeme.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceHelper private constructor(context: Context) {

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
        set(value) = sharedPref.edit { putString(KEY_USER_ID, value) }

    var userEmail: String?
        get() = sharedPref.getString(KEY_USER_EMAIL, null)
        set(value) = sharedPref.edit { putString(KEY_USER_EMAIL, value) }

    var userName: String?
        get() = sharedPref.getString(KEY_USER_NAME, null)
        set(value) = sharedPref.edit { putString(KEY_USER_NAME, value) }

    var fcmToken: String?
        get() = sharedPref.getString(KEY_FCM_TOKEN, null)
        set(value) = sharedPref.edit { putString(KEY_FCM_TOKEN, value) }

    fun clear() {
        sharedPref.edit { clear() }
    }
}
