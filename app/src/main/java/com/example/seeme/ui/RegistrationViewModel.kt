package com.example.seeme.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.google.firebase.auth.PhoneAuthProvider

class RegistrationViewModel : ViewModel() {
    private val _verificationId = MutableLiveData<String?>()
    val verificationId: LiveData<String?> = _verificationId

    fun setVerificationId(id: String) {
        _verificationId.value = id
    }

    fun clearVerificationId() {
        _verificationId.value = null
    }
}
