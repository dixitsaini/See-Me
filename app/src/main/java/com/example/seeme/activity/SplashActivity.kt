package com.example.seeme.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.seeme.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserSession()
        }, 2000)
    }

    private fun checkUserSession() {
//        if (prefHelper.userId != null) {
            startActivity(Intent(this, MainActivity::class.java))
//        } else {
//            startActivity(Intent(this, RegistrationActivity::class.java))
//        }
        finish()
    }
}
