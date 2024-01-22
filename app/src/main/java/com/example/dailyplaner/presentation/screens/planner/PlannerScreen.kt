package com.example.dailyplaner.presentation.screens.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailyplaner.data.models.to_do_short.TimeRange
import com.example.dailyplaner.data.models.to_do_short.ToDoShortView
import com.example.dailyplaner.presentation.ui.theme.DailyPlanerTheme
import com.example.dailyplaner.presentation.ui.theme.toDoItemSpacerHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(
    uiState: PlannerUiState,
    onEvent: (PlannerEvent) -> Unit,
    navigateToCreateToDo: (Long) -> Unit,
    navigateToToDoInfo: (Int) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.dateInMillis)

    LaunchedEffect(datePickerState.selectedDateMillis) {
        onEvent(PlannerEvent.UpdateToDoList(datePickerState.selectedDateMillis ?: 0))
    }
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column {
            DatePicker(state = datePickerState)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
            ) {
                item {

                }
                if (uiState.isLoading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(50.dp))
                        }
                    }

                } else {
                    if (uiState.toDoList.isEmpty()) {
                        item { Text(text = "Вы ещё не добавили ни одного дела на эту дату :(") }
                    } else {
                        uiState.toDoList.forEach { map ->
                            item {
                                TimeHeader(time = "${map.key.startTime}-${map.key.endTime}")
                                Spacer(modifier = Modifier.height(toDoItemSpacerHeight))
                            }
                            items(map.value) {
                                ToDoItem(
                                    Modifier.fillMaxWidth(),
                                    it
                                ) { toDoId -> navigateToToDoInfo(toDoId) }
                                Spacer(modifier = Modifier.height(toDoItemSpacerHeight))
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(all = 16.dp)
                .align(alignment = Alignment.BottomEnd),
            onClick = {
                navigateToCreateToDo(uiState.dateInMillis)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Outlined.Add, contentDescription = "Добавить дело")
        }
    }
}

@Composable
fun TimeHeader(time: String) {
    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(Modifier.weight(1f))
        Text(text = time)
        Divider(Modifier.weight(1f))
    }
}

@Composable
fun ToDoItem(modifier: Modifier, toDo: ToDoShortView, onClick: (Int) -> Unit) {
    Column(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
            .clickable {
                onClick(toDo.id)
            }
            .padding(vertical = 4.dp, horizontal = 10.dp),
    ) {
        Text(text = toDo.name, maxLines = 1)
        Text(text = if (toDo.timeStart == toDo.timeFinish) toDo.timeStart else "${toDo.timeStart}-${toDo.timeFinish}")
    }
}

@Preview(showBackground = true)
@Composable
fun TimeHeaderPreview() {
    TimeHeader("12.00-13.00")
}

@Preview(showBackground = true)
@Composable
fun ToDoItemPreview() {
    ToDoItem(Modifier, ToDoShortView(1, "12.00", "12.00", "Сделать зарядку"), {})
}

@Preview(showBackground = true)
@Composable
fun PlannerScreenDayPreview() {
    DailyPlanerTheme {
        PlannerScreen(
            uiState = PlannerUiState(
                System.currentTimeMillis(), mapOf(
                    TimeRange("12.00", "13.00") to listOf(
                        ToDoShortView(1, "12.00", "12.00", "Сделать зарядку аааааааааааааааааааааааааааааааааааааааааааааааааааааааааа"),
                        ToDoShortView(2, "12.14", "12.16", "Сделать зарядку"),
                        ToDoShortView(3, "12.17", "12.18", "Сделать зарядку"),
                    )
                ),
                false
            ),
            {},
            {},
            {}
        )
    }
}