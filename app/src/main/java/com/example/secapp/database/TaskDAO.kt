package com.example.secapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TasksDao {
    @Query("SELECT * FROM TasksEntity")
    fun getAll(): LiveData<List<TasksEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg task: TasksEntity)

    @Query("SELECT * FROM TasksEntity WHERE taskName = :taskName")
    fun getTask(taskName: String): LiveData<TasksEntity>
}