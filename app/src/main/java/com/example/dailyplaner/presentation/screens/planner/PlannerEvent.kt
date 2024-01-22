package com.example.dailyplaner.presentation.screens.planner

sealed class PlannerEvent {
    class UpdateToDoList(val newDateInMillis: Long) : PlannerEvent()
}