package com.example.dailyplaner.presentation.screens.planner

import com.example.dailyplaner.data.models.to_do_short.TimeRange
import com.example.dailyplaner.data.models.to_do_short.ToDoShortView
import java.util.Calendar

data class PlannerUiState(
    val dateInMillis: Long = Calendar.getInstance().timeInMillis,
    val toDoList: Map<TimeRange, List<ToDoShortView>>,
    val isLoading: Boolean = false,
    val error: String? = null
)