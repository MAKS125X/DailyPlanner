package com.example.dailyplaner.presentation.screens.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dailyplaner.data.ToDoRepository
import com.example.dailyplaner.di.DailyPlannerApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlannerViewModel(private val repository: ToDoRepository) : ViewModel() {

    private var _uiState: MutableStateFlow<PlannerUiState> =
        MutableStateFlow(PlannerUiState(toDoList = mapOf()))
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)

    private var getToDosJob: Job? = null

    fun onEvent(event: PlannerEvent) {
        when (event) {
            is PlannerEvent.UpdateToDoList -> {
                _uiState.update {
                    it.copy(dateInMillis = event.newDateInMillis)
                }
                updateToDoList()
            }
        }
    }

    init {
        updateToDoList()
    }

    private fun updateToDoList() {
        getToDosJob?.cancel()

        _uiState.update { it.copy(isLoading = true) }
        getToDosJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getToDoShortMap(_uiState.value.dateInMillis).collect {
                _uiState.update { uiState ->
                    uiState.copy(
                        toDoList = it,
                        isLoading = false
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DailyPlannerApplication)

                val repository = application.appModule.toDoRepository

                PlannerViewModel(repository = repository)
            }
        }
    }
}