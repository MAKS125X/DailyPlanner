package com.example.dailyplaner.presentation.utils

fun formatTime(hour: Int, minute: Int): String =
    "${if (hour < 10) "0$hour" else "$hour"}:${if (minute < 10) "0$minute" else "$minute"}"