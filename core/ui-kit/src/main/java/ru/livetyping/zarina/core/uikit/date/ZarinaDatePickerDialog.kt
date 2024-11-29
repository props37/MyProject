package ru.livetyping.zarina.core.uikit.date

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ZarinaDatePickerDialog(
    onDismissRequest: () -> Unit,
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    shape: Shape = ZarinaDatePickerDialogDefaults.Shape,
    colors: DatePickerColors = ZarinaDatePickerDefaults.colors(),
    properties: DialogProperties = remember { DialogProperties(usePlatformDefaultWidth = false) },
) {
    ZarinaDatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            ZarinaDatePickerDefaults.ConfirmButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                },
            )
        },
        dismissButton = dismissButton,
        shape = shape,
        colors = colors,
        properties = properties,
        modifier = modifier,
    ) {
        ZarinaDatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ZarinaDatePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    shape: Shape = ZarinaDatePickerDialogDefaults.Shape,
    colors: DatePickerColors = ZarinaDatePickerDefaults.colors(),
    properties: DialogProperties = remember { DialogProperties(usePlatformDefaultWidth = false) },
    content: @Composable ColumnScope.() -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        shape = shape,
        colors = colors,
        properties = properties,
        modifier = modifier,
        content = content,
    )
}

public object ZarinaDatePickerDialogDefaults {
    public val Shape: Shape get() = RoundedCornerShape(2.dp)
}
