package com.example.dailyplaner.presentation.screens.to_do_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismissRequest: () -> Unit,
    onHideRequest: () -> Unit,
    onConfirmRequest: (Int, Int) -> Unit,
) {
    val theme = MaterialTheme.colorScheme

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute
    )

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = theme.background,
                shape = RoundedCornerShape(size = 12.dp)
            ),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.LightGray.copy(alpha = 0.3f)
                )
                .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = theme.secondary,
                    clockDialSelectedContentColor = theme.primary,
                    clockDialUnselectedContentColor = theme.primary,
                    selectorColor = theme.tertiary,
                    containerColor = theme.background,
                    timeSelectorSelectedContainerColor = theme.primary,
                    timeSelectorUnselectedContainerColor = theme.tertiary,
                    timeSelectorSelectedContentColor = theme.onPrimary,
                    timeSelectorUnselectedContentColor = theme.onTertiaryContainer
                )
            )
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onHideRequest) {
                    Text(text = "Скрыть")
                }
                TextButton(
                    onClick = {
                        onConfirmRequest(timePickerState.hour, timePickerState.minute)
                    }
                ) {
                    Text(text = "Подтвердить")
                }
            }
        }
    }
}