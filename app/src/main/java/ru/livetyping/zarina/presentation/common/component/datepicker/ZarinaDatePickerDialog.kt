package ru.livetyping.zarina.presentation.common.component.datepicker

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZarinaDatePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    shape: Shape = Shape,
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

private val Shape: Shape get() = RoundedCornerShape(2.dp)
