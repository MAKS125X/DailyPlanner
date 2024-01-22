package com.example.dailyplaner.data.models.to_do_long

import androidx.room.ColumnInfo
import com.example.dailyplaner.data.local.ToDoEntity

data class ToDoLong(
    val id: Int,
    @ColumnInfo(name = "date_start") val dateStart: Long,
    @ColumnInfo(name = "date_finish") val dateFinish: Long,
    val name: String,
    val description: String
) {
    fun toEntity(): ToDoEntity = ToDoEntity(id, dateStart, dateFinish, name, description)
}