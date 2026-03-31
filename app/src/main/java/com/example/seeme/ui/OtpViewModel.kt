package com.example.seeme.ui

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import java.util.Locale

class OtpViewModel : ViewModel() {

    val otp = MutableLiveData<String>("")

    private val _verificationId = MutableLiveData<String?>()
    val verificationId: LiveData<String?> = _verificationId

    private val _timerText = MutableLiveData<String>("")
    val timerText: LiveData<String> = _timerText

    private val _canResend = MutableLiveData<Boolean>(false)
    val canResend: LiveData<Boolean> = _canResend

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var countDownTimer: CountDownTimer? = null

    // Callback functions for navigation and actions
    var onVerifyOtpClick: (() -> Unit)? = null
    var onResendOtpClick: (() -> Unit)? = null

    val isVerifyOtpEnabled: LiveData<Boolean> = otp.map { otpCode ->
        otpCode?.length == 6 && _isLoading.value == false
    }

    fun setVerificationId(id: String) {
        _verificationId.value = id
        startResendTimer()
    }

    fun startResendTimer(seconds: Long = 30) {
        _canResend.value = false
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val totalSecondsRemaining = millisUntilFinished / 1000
                val minutes = totalSecondsRemaining / 60
                val secondsRemaining = totalSecondsRemaining % 60
                _timerText.value = String.format(Locale.getDefault(), "%02d:%02d", minutes, secondsRemaining)
            }

            override fun onFinish() {
                _timerText.value = ""
                _canResend.value = true
            }
        }.start()
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun onVerifyOtpClick() {
        if (isVerifyOtpEnabled.value == true) {
            onVerifyOtpClick?.invoke()
        }
    }

    fun onResendOtpClick() {
        if (_canResend.value == true) {
            onResendOtpClick?.invoke()
        }
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }
}
