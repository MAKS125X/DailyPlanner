package com.example.dailyplaner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyplaner.data.models.to_do_long.ToDoLong
import com.example.dailyplaner.data.models.to_do_short.ToDoShort
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("Select id, date_start, date_finish, name FROM to_do WHERE date_start >= :dateStart AND date_finish < :dateFinish ORDER BY date_start")
    fun getAllToDos(dateStart: Long, dateFinish: Long): Flow<List<ToDoShort>>

    @Query("Select * FROM to_do WHERE id = :toDoId")
    suspend fun getToDoById(toDoId: Int): ToDoLong?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(toDo: ToDoEntity)

    @Query("DELETE FROM to_do WHERE id = :toDoId")
    suspend fun deleteToDoById(toDoId: Int): Int
}