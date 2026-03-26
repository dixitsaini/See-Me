package com.example.seeme.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClickCounterDao {
    @Insert
    suspend fun insert(clickCounter: ClickCounter)

    @Update
    suspend fun update(clickCounter: ClickCounter)

    @Query("SELECT * FROM click_counter WHERE id = 1 LIMIT 1")
    fun getClickCounter(): Flow<ClickCounter?>

    @Query("UPDATE click_counter SET clickCount = clickCount + 1 WHERE id = 1")
    suspend fun incrementClick()

    @Query("DELETE FROM click_counter")
    suspend fun clear()
}
