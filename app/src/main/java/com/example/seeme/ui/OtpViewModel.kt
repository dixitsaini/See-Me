package com.example.seeme.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class OtpViewModel : ViewModel() {

    val otp = MutableLiveData<String>("")

    private val _verificationId = MutableLiveData<String?>()
    val verificationId: LiveData<String?> = _verificationId

    // Callback functions for navigation and actions
    var onVerifyOtpClick: (() -> Unit)? = null
    var onResendOtpClick: (() -> Unit)? = null
    var onBackClick: (() -> Unit)? = null

    val isVerifyOtpEnabled: LiveData<Boolean> = otp.map { otpCode ->
        otpCode?.length == 6
    }

    fun setVerificationId(id: String) {
        _verificationId.value = id
    }

    fun onVerifyOtpClick() {
        onVerifyOtpClick?.invoke()
    }

    fun onResendOtpClick() {
        onResendOtpClick?.invoke()
    }

    fun onBackClick() {
        onBackClick?.invoke()
    }
}