package com.example.dailyplaner.di

import android.app.Application
import androidx.room.Room
import com.example.dailyplaner.data.ToDoRepository
import com.example.dailyplaner.data.local.AppDatabase

class AppModule(app: Application) {

    private val localDataSource = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "planner_database.db"
    ).build()

    val toDoRepository = ToDoRepository(localDataSource)
}