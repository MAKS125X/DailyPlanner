package com.example.dailyplaner.presentation.screens.to_do_info

data class ToDoInfoState(
    val currentToDoId: Int? = null,

    val aboutText: String = "",

    val showStartTimePicker: Boolean = false,
    val showFinishTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,

    val dateInMillis: Long = 0,
    val dateInMillisError: String = "",
    val showDateInMillisError: Boolean = false,

    val startHour: Int = 0,
    val startMinute: Int = 0,
    val startMinuteError: String = "",
    val showStartMinuteError: Boolean = false,

    val finishHour: Int = 0,
    val finishMinute: Int = 0,
    val finishMinuteError: String = "",
    val showFinishMinuteError: Boolean = false,

    val nameText: String = "",
    val nameTextError: String = "",
    val showNameTextError: Boolean = false,

    val descriptionText: String = "",
    val descriptionTextError: String = "",
    val showDescriptionTextError: Boolean = false,

    val isLoading: Boolean = false,

    val showDeleteToDoDialog: Boolean = false,

    val addResult: AddingResult = AddingResult.Nothing,
    val deleteResult: DeleteResult = DeleteResult.Nothing
) {
    sealed class AddingResult {
        object Success : AddingResult()
        class DatabaseError(val errorMessage: String) : AddingResult()
        class ValidationError(val errorMessage: String) : AddingResult()
        object Nothing : AddingResult()
    }

    sealed class DeleteResult {
        class Success(countOfDeletedRows: Int) : DeleteResult()
        class DatabaseError(val errorMessage: String) : DeleteResult()
        object Nothing : DeleteResult()
    }
}

