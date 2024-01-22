package com.example.dailyplaner.presentation.screens.to_do_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dailyplaner.data.ToDoRepository
import com.example.dailyplaner.data.exceptions.EmptyToDoNameException
import com.example.dailyplaner.data.exceptions.InvalidToDoDateRangeException
import com.example.dailyplaner.data.models.to_do_long.ToDoLongView
import com.example.dailyplaner.di.DailyPlannerApplication
import com.example.dailyplaner.presentation.navigation.Destinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoInfoViewModel(
    private val repository: ToDoRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _uiState: MutableStateFlow<ToDoInfoState> = MutableStateFlow(ToDoInfoState())
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)

    init {
        savedStateHandle.get<Int>(Destinations.TO_DO_INFO_ID)?.let { toDoId ->
            if (toDoId != -1) {
                viewModelScope.launch {
                    repository.getToDoLong(toDoId)?.also { toDo ->
                        _uiState.update {
                            it.copy(
                                currentToDoId = toDo.id,
                                dateInMillis = toDo.dateInMillis - toDo.dateInMillis % (24 * 60 * 60 * 1000),
                                startHour = toDo.hourStart,
                                startMinute = toDo.minuteStart,
                                finishHour = toDo.hourFinish,
                                finishMinute = toDo.minuteFinish,
                                nameText = toDo.name,
                                descriptionText = toDo.description
                            )
                        }
                    }
                }
            } else {
                val currentMillis =
                    savedStateHandle.get<Long>(Destinations.TO_DO_INFO_DATE)
                        ?: System.currentTimeMillis()
                _uiState.update {
                    it.copy(
                        dateInMillis = currentMillis - currentMillis % (24 * 60 * 60 * 1000),
                        currentToDoId = -1
                    )
                }
            }
        }
    }

    fun onEvent(event: ToDoInfoEvent) {
        when (event) {

            ToDoInfoEvent.CloseTimeStartPicked -> {
                _uiState.update { it.copy(showStartTimePicker = false) }
            }

            ToDoInfoEvent.OpenStartTimePicker -> {
                _uiState.update { it.copy(showStartTimePicker = true) }
            }

            is ToDoInfoEvent.UpdateTimeStart -> {
                _uiState.update {
                    it.copy(
                        startHour = event.hour, startMinute = event.minute,
                        showStartTimePicker = false
                    )
                }
                clearDateRangeError()
            }

            ToDoInfoEvent.CloseTimeFinishPicked -> {
                _uiState.update { it.copy(showFinishTimePicker = false) }
            }

            ToDoInfoEvent.OpenFinishTimePicker -> {
                _uiState.update { it.copy(showFinishTimePicker = true) }
            }

            is ToDoInfoEvent.UpdateTimeFinish -> {
                _uiState.update {
                    it.copy(
                        finishHour = event.hour, finishMinute = event.minute,
                        showFinishTimePicker = false
                    )
                }
                clearDateRangeError()
            }

            ToDoInfoEvent.OpenDatePicker -> {
                _uiState.update { it.copy(showDatePicker = true) }
            }

            ToDoInfoEvent.CloseDatePicker -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            is ToDoInfoEvent.UpdateDate -> {
                _uiState.update {
                    it.copy(dateInMillis = event.millis, showDatePicker = false)
                }
            }

            is ToDoInfoEvent.ChangeDescription -> {
                _uiState.update { it.copy(descriptionText = event.description) }
            }

            is ToDoInfoEvent.ChangeName -> {
                _uiState.update { it.copy(nameText = event.name) }
                clearNameTextError()
            }

            ToDoInfoEvent.SaveToDo -> {
                saveToDo()
            }

            ToDoInfoEvent.ShowValidationError -> {}

            ToDoInfoEvent.DeleteToDo -> {
                deleteToDo()
            }

            ToDoInfoEvent.OpenDeleteDialog -> {
                _uiState.update {
                    it.copy(
                        showDeleteToDoDialog = true
                    )
                }
            }

            ToDoInfoEvent.CloseDeleteDialog -> {
                _uiState.update {
                    it.copy(
                        showDeleteToDoDialog = false
                    )
                }
            }
        }
    }

    private fun saveToDo() {
        viewModelScope.launch {
            try {
                val id = _uiState.value.currentToDoId
                repository.saveToDo(
                    ToDoLongView(
                        when (id) {
                            null -> 0
                            -1 -> 0
                            else -> id
                        },
                        _uiState.value.dateInMillis,
                        _uiState.value.startHour,
                        _uiState.value.startMinute,
                        _uiState.value.finishHour,
                        _uiState.value.finishMinute,
                        _uiState.value.nameText,
                        _uiState.value.descriptionText,
                    )
                )
                _uiState.update { it.copy(addResult = ToDoInfoState.AddingResult.Success) }
            } catch (e: EmptyToDoNameException) {
                _uiState.update {
                    it.copy(
                        showNameTextError = true,
                        nameTextError = e.message ?: "Некорректные данные",
                        addResult = ToDoInfoState.AddingResult.ValidationError(
                            e.message ?: "Некорректные данные"
                        )
                    )
                }
            } catch (e: InvalidToDoDateRangeException) {
                _uiState.update {
                    it.copy(
                        showStartMinuteError = true,
                        startMinuteError = e.message ?: "Некорректные данные",

                        showFinishMinuteError = true,
                        finishMinuteError = e.message ?: "Некорректные данные",

                        addResult = ToDoInfoState.AddingResult.ValidationError(
                            e.message ?: "Некорректные данные"
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        addResult = ToDoInfoState.AddingResult.DatabaseError(
                            e.message ?: "Ошибка добавления в базы данных"
                        )
                    )
                }
            }
        }
    }

    private fun deleteToDo() {
        val currentId = _uiState.value.currentToDoId

        if (currentId != null && currentId != -1) {
            viewModelScope.launch {
                try {
                    when (repository.deleteToDo(currentId)) {
                        0 -> _uiState.update {
                            it.copy(
                                deleteResult = ToDoInfoState.DeleteResult.DatabaseError(
                                    "Данное дело уже удалено"
                                )
                            )
                        }

                        1 -> _uiState.update {
                            it.copy(deleteResult = ToDoInfoState.DeleteResult.Success(1))
                        }

                        else -> _uiState.update {
                            it.copy(
                                deleteResult = ToDoInfoState.DeleteResult.DatabaseError(
                                    "Внутренняя ошибка, откройте дело снова"
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            deleteResult = ToDoInfoState.DeleteResult.DatabaseError(
                                e.message ?: "Ошибка добавления в базы данных"
                            )
                        )
                    }
                }
            }
        }
    }


    private fun clearNameTextError() {
        _uiState.update {
            it.copy(
                showNameTextError = false,
                nameTextError = "",
                addResult = ToDoInfoState.AddingResult.Nothing,
            )
        }
    }

    private fun clearDateRangeError() {
        _uiState.update {
            it.copy(
                showStartMinuteError = false,
                startMinuteError = "",

                showFinishMinuteError = false,
                finishMinuteError = "",
                addResult = ToDoInfoState.AddingResult.Nothing,
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DailyPlannerApplication)

                val repository = application.appModule.toDoRepository

                val savedStateHandle = createSavedStateHandle()

                ToDoInfoViewModel(repository = repository, savedStateHandle)
            }
        }
    }
}