package com.example.dailyplaner.data

import com.example.dailyplaner.data.exceptions.EmptyToDoNameException
import com.example.dailyplaner.data.exceptions.InvalidToDoDateRangeException
import com.example.dailyplaner.data.local.AppDatabase
import com.example.dailyplaner.data.local.ToDoDao
import com.example.dailyplaner.data.models.to_do_long.ToDoLongMapper
import com.example.dailyplaner.data.models.to_do_long.ToDoLongView
import com.example.dailyplaner.data.models.to_do_short.TimeRange
import com.example.dailyplaner.data.models.to_do_short.ToDoShortMapper
import com.example.dailyplaner.data.models.to_do_short.ToDoShortView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.TimeZone

class ToDoRepository(localDataSource: AppDatabase) {
    private val dao: ToDoDao = localDataSource.toDoDao()
    private val calendar = Calendar.getInstance()

    suspend fun getToDoShortMap(
        selectedDate: Long
    ): Flow<Map<TimeRange, List<ToDoShortView>>> {
        val dateStart = selectedDate - selectedDate % (24 * 60 * 60 * 1000)
        val dateFinish = dateStart + 24 * 60 * 60 * 1000
        return withContext(Dispatchers.IO) {
            return@withContext dao.getAllToDos(dateStart, dateFinish).map { list ->
                list.groupBy { getTimeRange(it.dateStart) }
                    .mapValues { (_, value) -> value.map { ToDoShortMapper.toViewItem(it) } }
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    suspend fun saveToDo(
        toDoLongView: ToDoLongView
    ) {

        if (toDoLongView.name.isEmpty()) {
            throw EmptyToDoNameException("Дело должно иметь имя")
        }
        if (toDoLongView.hourFinish < toDoLongView.hourStart ||
            toDoLongView.hourFinish == toDoLongView.hourStart && toDoLongView.minuteFinish < toDoLongView.minuteStart
        ) {
            throw InvalidToDoDateRangeException("Дело должно оканчиваться после начала")
        }

        val toDo = ToDoLongMapper.toDto(toDoLongView)
        val toDoDo = toDo.toEntity()
        dao.insertToDo(toDoDo)
    }

    suspend fun getToDoLong(id: Int): ToDoLongView? =
        dao.getToDoById(id)?.let { ToDoLongMapper.toViewItem(it) }

    suspend fun deleteToDo(id: Int): Int =
        dao.deleteToDoById(id)


    private fun getTimeRange(timestamp: Long): TimeRange {
        calendar.timeInMillis = timestamp
        calendar.timeZone = TimeZone.getTimeZone("UTC")

        val startHour = calendar.get(Calendar.HOUR_OF_DAY)
        val endHour = (startHour + 1) % 24

        val startTime = String.format("%02d:00", startHour)
        val endTime = String.format("%02d:00", endHour)

        return TimeRange(startTime, endTime)
    }
}