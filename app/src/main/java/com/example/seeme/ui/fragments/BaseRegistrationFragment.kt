package com.example.seeme.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.seeme.activity.MainActivity
import com.example.seeme.ui.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

open class BaseRegistrationFragment : BaseFragment() {

    protected val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    protected val viewModel: RegistrationViewModel by activityViewModels()

    protected fun saveUserAndProceed(firebaseUser: com.google.firebase.auth.FirebaseUser?) {
        if (firebaseUser == null) return

        val context = requireContext()
        val sharedPref = context.getSharedPreferences("SeemePrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_id", firebaseUser.uid)
            putString("user_email", firebaseUser.email)
            putString("user_name", firebaseUser.displayName)
            apply()
        }

        // Fetch FCM Token
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM", "Token: $token")
                    sharedPref.edit().putString("fcm_token", token).apply()
                } else {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                }
                // Navigate to MainActivity regardless of token success
                startActivity(Intent(context, MainActivity::class.java))
                requireActivity().finish()
            }
        } catch (e: Exception) {
            Log.e("FCM", "Error accessing Firebase Messaging", e)
            startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        }
    }
}
