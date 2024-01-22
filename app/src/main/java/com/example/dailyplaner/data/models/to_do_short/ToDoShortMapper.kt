package com.example.dailyplaner.data.models.to_do_short

import com.example.dailyplaner.data.mapper_base.DtoMapper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object ToDoShortMapper : DtoMapper<ToDoShort, ToDoShortView> {
    private val calendar: Calendar = Calendar.getInstance()

    override fun toViewItem(value: ToDoShort): ToDoShortView =
        ToDoShortView(
            value.id,
            convertToTimeFormat(value.dateStart),
            convertToTimeFormat(value.dateFinish),
            value.name
        )

    private fun convertToTimeFormat(timestamp: Long): String {
        calendar.timeInMillis = timestamp

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(calendar.timeInMillis)
    }
}