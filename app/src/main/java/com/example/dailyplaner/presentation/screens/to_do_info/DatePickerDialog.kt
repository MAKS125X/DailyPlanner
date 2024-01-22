package com.example.dailyplaner.presentation.screens.to_do_info

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.dailyplaner.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogImpl(
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
    initialTimeMillis: Long,
) {
    val state = rememberDatePickerState(initialTimeMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(state.selectedDateMillis ?: initialTimeMillis) }) {
                Text(text = stringResource(id = R.string.accept_button))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(id = R.string.cancel_button))
            }
        }
    ) {
        DatePicker(state = state)
    }
}