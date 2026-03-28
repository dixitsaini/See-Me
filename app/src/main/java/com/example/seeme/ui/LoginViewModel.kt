package com.example.seeme.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class LoginViewModel : ViewModel() {

    val phoneNumber = MutableLiveData<String>("")

    // Callback functions for navigation and actions
    var onSendOtpClick: (() -> Unit)? = null
    var onBackClick: (() -> Unit)? = null

    val isSendOtpEnabled: LiveData<Boolean> = phoneNumber.map { phone ->
        phone?.length == 10
    }

    fun onSendOtpClick() {
        onSendOtpClick?.invoke()
    }

    fun onBackClick() {
        onBackClick?.invoke()
    }
}