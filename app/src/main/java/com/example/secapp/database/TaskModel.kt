package com.example.secapp.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Tasks(
    val id: Long,
    val taskName: String,
    val description: String,
    val startDate: Date,
    val startTime: Date,
    val colorEvent: String,
    val colorEventInt: Int,
    val firm: String
) : Parcelable