package ru.livetyping.zarina.presentation.common.component.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.util.compose.none

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZarinaModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = ZarinaBottomSheetDefaults.Shape,
    containerColor: Color = ZarinaBottomSheetDefaults.ContainerColor,
    contentColor: Color = ZarinaBottomSheetDefaults.ContentColor,
    scrimColor: Color = ZarinaBottomSheetDefaults.ScrimColor,
    windowInsets: @Composable () -> WindowInsets = { WindowInsets.none },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = 0.dp,
        scrimColor = scrimColor,
        dragHandle = null,
        contentWindowInsets = windowInsets,
        modifier = modifier,
        content = content,
    )
}
