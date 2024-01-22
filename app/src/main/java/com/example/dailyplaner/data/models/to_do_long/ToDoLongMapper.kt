package com.example.dailyplaner.data.models.to_do_long

import com.example.dailyplaner.data.mapper_base.DtoMapper
import com.example.dailyplaner.data.mapper_base.ViewItemMapper
import java.util.Calendar
import java.util.TimeZone

object ToDoLongMapper : DtoMapper<ToDoLong, ToDoLongView>,
    ViewItemMapper<ToDoLongView, ToDoLong> {

    private val calendarTimeZone: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    override fun toViewItem(value: ToDoLong): ToDoLongView {
        return ToDoLongView(
            value.id,
            value.dateStart,
            getHour(value.dateStart),
            getMinute(value.dateStart),
            getHour(value.dateFinish),
            getMinute(value.dateFinish),
            value.name,
            value.description
        )
    }

    override fun toDto(item: ToDoLongView): ToDoLong {
        return ToDoLong(
            id = item.id,
            dateStart = getTimestamp(item.dateInMillis, item.hourStart, item.minuteStart),
            dateFinish = getTimestamp(item.dateInMillis, item.hourFinish, item.minuteFinish),
            name = item.name,
            description = item.description
        )
    }

    private fun getTimestamp(date: Long, hour: Int, minute: Int): Long {

        return date + hour * 60 * 60 * 1000 + minute * 60 * 1000
    }

    private fun getHour(timestamp: Long): Int {
        calendarTimeZone.timeInMillis = timestamp
        return calendarTimeZone.get(Calendar.HOUR_OF_DAY)
    }

    private fun getMinute(timestamp: Long): Int {
        calendarTimeZone.timeInMillis = timestamp
        return calendarTimeZone.get(Calendar.MINUTE)
    }
}