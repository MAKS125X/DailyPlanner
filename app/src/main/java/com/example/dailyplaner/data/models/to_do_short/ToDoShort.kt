package com.example.dailyplaner.data.models.to_do_short

import androidx.room.ColumnInfo

data class ToDoShort(
    val id: Int,
    @ColumnInfo(name = "date_start") val dateStart: Long,
    @ColumnInfo(name = "date_finish") val dateFinish: Long,
    val name: String,
)