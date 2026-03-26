package com.example.seeme.data

import kotlinx.coroutines.flow.Flow

class ClickCounterRepository(private val clickCounterDao: ClickCounterDao) {

    val clickCounter: Flow<ClickCounter?> = clickCounterDao.getClickCounter()

    suspend fun initializeIfNeeded() {
        val current = clickCounterDao.getClickCounter()
        // The initialization is handled in ViewModel
    }

    suspend fun incrementClick() {
        clickCounterDao.incrementClick()
    }

    suspend fun insertClickCounter(clickCounter: ClickCounter) {
        clickCounterDao.insert(clickCounter)
    }

    suspend fun clearClicks() {
        clickCounterDao.clear()
    }
}
