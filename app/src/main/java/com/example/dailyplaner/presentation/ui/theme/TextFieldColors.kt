package com.example.dailyplaner.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onBackground,
    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
    disabledTextColor = MaterialTheme.colorScheme.onBackground,
    errorTextColor = MaterialTheme.colorScheme.error,

    focusedContainerColor = MaterialTheme.colorScheme.background,
    unfocusedContainerColor = MaterialTheme.colorScheme.background,
    disabledContainerColor = MaterialTheme.colorScheme.background,
    errorContainerColor = MaterialTheme.colorScheme.onErrorContainer,

    focusedSupportingTextColor = Color.Transparent,
    unfocusedSupportingTextColor = Color.Transparent,
    disabledSupportingTextColor = Color.Transparent,
    errorSupportingTextColor = MaterialTheme.colorScheme.error,

    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
    errorBorderColor = MaterialTheme.colorScheme.errorContainer,

    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
    disabledLabelColor = MaterialTheme.colorScheme.onBackground,
    errorLabelColor = MaterialTheme.colorScheme.error,

    focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
    disabledTrailingIconColor = MaterialTheme.colorScheme.onBackground,
    errorTrailingIconColor = MaterialTheme.colorScheme.onBackground,
)