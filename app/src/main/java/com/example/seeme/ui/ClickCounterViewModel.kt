package com.example.seeme.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.seeme.data.ClickCounter
import com.example.seeme.data.ClickCounterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClickCounterViewModel(private val repository: ClickCounterRepository) : ViewModel() {

    private val _clickCounter = MutableStateFlow<Int>(0)
    val clickCounter = _clickCounter.asStateFlow()

    init {
        viewModelScope.launch {
            repository.clickCounter.collect { counter ->
                _clickCounter.value = counter?.clickCount ?: 0
                // Initialize counter if it's null
                if (counter == null) {
                    repository.insertClickCounter(ClickCounter(id = 1, clickCount = 0))
                }
            }
        }
    }

    fun onClickButtonPressed() {
        viewModelScope.launch {
            repository.incrementClick()
        }
    }

    fun resetClicks() {
        viewModelScope.launch {
            repository.clearClicks()
            repository.insertClickCounter(ClickCounter(id = 1, clickCount = 0))
        }
    }
}

class ClickCounterViewModelFactory(private val repository: ClickCounterRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClickCounterViewModel::class.java)) {
            return ClickCounterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
