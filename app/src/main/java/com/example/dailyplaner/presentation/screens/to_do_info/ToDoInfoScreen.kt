package com.example.dailyplaner.presentation.screens.to_do_info

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailyplaner.R
import com.example.dailyplaner.presentation.ui.theme.DailyPlanerTheme
import com.example.dailyplaner.presentation.ui.theme.outlinedTextFieldColors
import com.example.dailyplaner.presentation.ui.theme.verticalTextFieldSpacerHeight
import com.example.dailyplaner.presentation.utils.formatTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ToDoInfoScreen(
    uiState: ToDoInfoState,
    onToDoEvent: (ToDoInfoEvent) -> Unit,
    navigateUp: () -> Unit,
) {

    val dateFormatter = SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.getDefault())
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    when (val result = uiState.addResult) {
        is ToDoInfoState.AddingResult.DatabaseError -> {
            LaunchedEffect(uiState.addResult) {
                Toast.makeText(context, result.errorMessage.asString(context), Toast.LENGTH_LONG).show()
            }
        }

        ToDoInfoState.AddingResult.Success -> {
            navigateUp()
        }

        ToDoInfoState.AddingResult.Nothing -> {}
        is ToDoInfoState.AddingResult.ValidationError -> {}
    }

    when (val result = uiState.deleteResult) {
        is ToDoInfoState.DeleteResult.DatabaseError -> {
            LaunchedEffect(uiState.addResult) {
                Toast.makeText(context, result.errorMessage.asString(context), Toast.LENGTH_LONG)
                    .show()
            }
        }

        is ToDoInfoState.DeleteResult.Success -> {
            onToDoEvent(ToDoInfoEvent.CloseDeleteDialog)
            navigateUp()
        }

        ToDoInfoState.DeleteResult.Nothing -> {}
    }

    if (uiState.showStartTimePicker) {
        TimePickerDialog(
            initialHour = uiState.startHour,
            initialMinute = uiState.startMinute,
            onDismissRequest = { onToDoEvent(ToDoInfoEvent.CloseTimeStartPicked) },
            onHideRequest = { onToDoEvent(ToDoInfoEvent.CloseTimeStartPicked) },
            onConfirmRequest = { hour, minute ->
                onToDoEvent(ToDoInfoEvent.UpdateTimeStart(hour, minute))
            })
    }

    if (uiState.showFinishTimePicker) {
        TimePickerDialog(
            initialHour = uiState.finishHour,
            initialMinute = uiState.finishMinute,
            onDismissRequest = { onToDoEvent(ToDoInfoEvent.CloseTimeFinishPicked) },
            onHideRequest = { onToDoEvent(ToDoInfoEvent.CloseTimeFinishPicked) },
            onConfirmRequest = { hour, minute ->
                onToDoEvent(ToDoInfoEvent.UpdateTimeFinish(hour, minute))
            }
        )
    }

    if (uiState.showDatePicker) {
        DatePickerDialogImpl(
            { onToDoEvent(ToDoInfoEvent.CloseDatePicker) },
            { onToDoEvent(ToDoInfoEvent.UpdateDate(it)) },
            uiState.dateInMillis
        )
    }

    if (uiState.showDeleteToDoDialog) {
        DeleteToDoAlertDialog(
            onDismissRequest = { onToDoEvent(ToDoInfoEvent.CloseDeleteDialog) },
            onConfirmation = { onToDoEvent(ToDoInfoEvent.DeleteToDo) }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { navigateUp() },
                    colors = IconButtonDefaults
                        .iconButtonColors(contentColor = colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.close_content_description)
                    )
                }
            }
            if (uiState.currentToDoId == -1) {
                Text(
                    text = stringResource(R.string.add_to_do),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            OutlinedTextField(
                value = uiState.nameText,
                onValueChange = { onToDoEvent(ToDoInfoEvent.ChangeName(it)) },
                label = { Text(stringResource(R.string.to_do_name)) },
                supportingText = { Text(text = uiState.nameTextError.asString()) },
                maxLines = 2,
                isError = uiState.showNameTextError,
                shape = RoundedCornerShape(16.dp),

                colors = outlinedTextFieldColors(),
                modifier = Modifier.width(280.dp),
            )
            Spacer(modifier = Modifier.height(verticalTextFieldSpacerHeight))
            OutlinedTextField(
                value = dateFormatter.format(Date(uiState.dateInMillis)) ?: "",
                onValueChange = {},
                label = { Text(stringResource(R.string.to_do_date)) },
                readOnly = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(
                        onClick = { onToDoEvent(ToDoInfoEvent.OpenDatePicker) },
                        colors = if (uiState.showDateInMillisError)
                            IconButtonDefaults.iconButtonColors(
                                contentColor = colorScheme.error
                            )
                        else IconButtonDefaults.iconButtonColors()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = stringResource(R.string.change_date_button),
                        )
                    }
                },
                supportingText = { Text(text = uiState.dateInMillisError.asString()) },
                isError = uiState.showDateInMillisError,
                colors = outlinedTextFieldColors()
            )
            Spacer(modifier = Modifier.height(verticalTextFieldSpacerHeight))
            OutlinedTextField(
                value = formatTime(uiState.startHour, uiState.startMinute),
                onValueChange = {},
                label = { Text(stringResource(R.string.start_time)) },
                readOnly = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(
                        onClick = { onToDoEvent(ToDoInfoEvent.OpenStartTimePicker) },
                        colors = if (uiState.showStartMinuteError)
                            IconButtonDefaults.iconButtonColors(
                                contentColor = colorScheme.error
                            )
                        else IconButtonDefaults.iconButtonColors()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.clock),
                            contentDescription = stringResource(R.string.change_start_time),
                            modifier = Modifier.fillMaxSize(0.6f)
                        )
                    }
                },
                supportingText = { Text(text = uiState.startMinuteError.asString()) },
                isError = uiState.showStartMinuteError,
                colors = outlinedTextFieldColors()
            )
            Spacer(modifier = Modifier.height(verticalTextFieldSpacerHeight))
            OutlinedTextField(
                value = formatTime(uiState.finishHour, uiState.finishMinute),
                onValueChange = {},
                label = { Text(stringResource(R.string.finish_time)) },
                readOnly = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(
                        onClick = { onToDoEvent(ToDoInfoEvent.OpenFinishTimePicker) },
                        colors = if (uiState.showFinishMinuteError)
                            IconButtonDefaults.iconButtonColors(
                                contentColor = colorScheme.error
                            )
                        else IconButtonDefaults.iconButtonColors()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.clock),
                            contentDescription = stringResource(R.string.change_finish_time),
                            modifier = Modifier.fillMaxSize(0.6f)
                        )
                    }
                },
                supportingText = { Text(text = uiState.finishMinuteError.asString()) },
                isError = uiState.showFinishMinuteError,
                colors = outlinedTextFieldColors()
            )
            Spacer(modifier = Modifier.height(verticalTextFieldSpacerHeight))
            OutlinedTextField(
                value = uiState.descriptionText,
                onValueChange = { onToDoEvent(ToDoInfoEvent.ChangeDescription(it)) },
                label = { Text(stringResource(R.string.description)) },
                shape = RoundedCornerShape(16.dp),
                supportingText = { Text(text = "") },
                colors = outlinedTextFieldColors(),
                modifier = Modifier.width(280.dp),
                maxLines = 7
            )
            Spacer(modifier = Modifier.height(verticalTextFieldSpacerHeight))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.width(280.dp)
            ) {
                Row(modifier = Modifier.weight(0.25f)) { }
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { onToDoEvent(ToDoInfoEvent.SaveToDo) },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorScheme.onPrimary,
                            containerColor = colorScheme.primary
                        )
                    ) {
                        Text(
                            text = if (uiState.currentToDoId == -1)
                                stringResource(R.string.add_button)
                            else stringResource(R.string.update_button)
                        )
                    }
                }

                Row(modifier = Modifier.weight(0.25f), horizontalArrangement = Arrangement.End) {
                    if (uiState.currentToDoId != -1) {
                        IconButton(
                            onClick = { onToDoEvent(ToDoInfoEvent.OpenDeleteDialog) },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = colorScheme.errorContainer,
                                contentColor = colorScheme.onErrorContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.delete_to_do)
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, device = PIXEL_3_XL)
@Composable
fun ToDoInfoStateDayPreview() {
    DailyPlanerTheme {
        ToDoInfoScreen(
            uiState = ToDoInfoState(
                currentToDoId = -1,
                startHour = 0,
                startMinute = 0,
                finishHour = 0,
                finishMinute = 0,
                nameText = "Текст aaaa Текст aaaaaaaa Текст aaa aaaaaaaaaa",
                descriptionText = "aaaaaaaaaaaaaaa aaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaa\n" +
                        "aa aaaaaa aaaaaa aaaaaaaaa aaaa  aaa aaaaaa aaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaa aaaa aaaaaa aaaaaa" +
                        "aaaaaaaaa aaaaaaaaa aaaaaaaaaa aaaaaaaaa aaaaaaa aaaaaaa" +
                        "aaaaaaaaa aaaaaaaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaa aaaaaaa aaaaaaaaaaaaaaaaaaaaaaaaaaa aaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaa aaaa aaaaaaaaaaaaaa aaaaaaaaaa"
            ),
            {},
            {}
        )
    }
}