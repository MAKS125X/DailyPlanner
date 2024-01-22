package com.example.dailyplaner.presentation.screens.to_do_info

import com.example.dailyplaner.presentation.ui.theme.UiText

data class ToDoInfoState(
    val currentToDoId: Int? = null,

    val aboutText: String = "",

    val showStartTimePicker: Boolean = false,
    val showFinishTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,

    val dateInMillis: Long = 0,
    val dateInMillisError: UiText = UiText.DynamicString(""),
    val showDateInMillisError: Boolean = false,

    val startHour: Int = 0,
    val startMinute: Int = 0,
    val startMinuteError: UiText = UiText.DynamicString(""),
    val showStartMinuteError: Boolean = false,

    val finishHour: Int = 0,
    val finishMinute: Int = 0,
    val finishMinuteError: UiText = UiText.DynamicString(""),
    val showFinishMinuteError: Boolean = false,

    val nameText: String = "",
    val nameTextError: UiText = UiText.DynamicString(""),
    val showNameTextError: Boolean = false,

    val descriptionText: String = "",
    val descriptionTextError: UiText = UiText.DynamicString(""),
    val showDescriptionTextError: Boolean = false,

    val isLoading: Boolean = false,

    val showDeleteToDoDialog: Boolean = false,

    val addResult: AddingResult = AddingResult.Nothing,
    val deleteResult: DeleteResult = DeleteResult.Nothing
) {
    sealed class AddingResult {
        object Success : AddingResult()
        class DatabaseError(val errorMessage: UiText) : AddingResult()
        class ValidationError(val errorMessage: UiText) : AddingResult()
        object Nothing : AddingResult()
    }

    sealed class DeleteResult {
        class Success(countOfDeletedRows: Int) : DeleteResult()
        class DatabaseError(val errorMessage: UiText) : DeleteResult()
        object Nothing : DeleteResult()
    }
}