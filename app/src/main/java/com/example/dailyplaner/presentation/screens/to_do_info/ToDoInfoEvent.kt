package com.example.dailyplaner.presentation.screens.to_do_info

sealed class ToDoInfoEvent {

    data class UpdateTimeStart(val hour: Int, val minute: Int) : ToDoInfoEvent()
    data class UpdateTimeFinish(val hour: Int, val minute: Int) : ToDoInfoEvent()
    data class UpdateDate(val millis: Long) : ToDoInfoEvent()

    data class ChangeName(val name: String) : ToDoInfoEvent()
    data class ChangeDescription(val description: String) : ToDoInfoEvent()

    object OpenDatePicker : ToDoInfoEvent()
    object CloseDatePicker : ToDoInfoEvent()

    object OpenStartTimePicker : ToDoInfoEvent()
    object CloseTimeStartPicked : ToDoInfoEvent()

    object OpenFinishTimePicker : ToDoInfoEvent()
    object CloseTimeFinishPicked : ToDoInfoEvent()

    object SaveToDo : ToDoInfoEvent()

    object OpenDeleteDialog : ToDoInfoEvent()
    object CloseDeleteDialog : ToDoInfoEvent()
    object DeleteToDo : ToDoInfoEvent()

    object ShowValidationError : ToDoInfoEvent()
}