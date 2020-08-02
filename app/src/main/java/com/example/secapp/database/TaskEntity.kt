package com.example.secapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TasksEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "_id") var id: Long = 0L,
    @ColumnInfo(name = "taskName") val taskName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "startDate") val startDate: Date,
    @ColumnInfo(name = "startTime") val startTime: Date,
    @ColumnInfo(name = "colorEvent") val colorEvent: String,
    @ColumnInfo(name = "colorEventInt") val colorEventInt: Int,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "firm") val firm: String
    )