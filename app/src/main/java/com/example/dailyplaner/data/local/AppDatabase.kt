package com.example.dailyplaner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [ToDoEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}