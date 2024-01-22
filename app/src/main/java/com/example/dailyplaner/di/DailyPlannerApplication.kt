package com.example.dailyplaner.di

import android.app.Application

class DailyPlannerApplication : Application() {

    private lateinit var _appModule: AppModule
    val appModule: AppModule
        get() = _appModule

    override fun onCreate() {
        super.onCreate()

        _appModule = provideAppModule()
    }

    private fun provideAppModule() = AppModule(this)
}