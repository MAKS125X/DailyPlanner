package com.example.dailyplaner.data.models.to_do_long

class ToDoLongView(
    val id: Int,
    val dateInMillis: Long,
    val hourStart: Int,
    val minuteStart: Int,
    val hourFinish: Int,
    val minuteFinish: Int,
    val name: String,
    val description: String
)