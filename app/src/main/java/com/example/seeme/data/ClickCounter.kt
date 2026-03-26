package com.example.seeme.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "click_counter")
data class ClickCounter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clickCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
