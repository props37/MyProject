package ru.zarina.zarina.utils.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.max

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(direction) + other.calculateStartPadding(direction),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(direction) + other.calculateEndPadding(direction),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
    )
}

@Composable
fun max(first: PaddingValues, second: PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current
    return PaddingValues(
        start = max(
            first.calculateStartPadding(direction),
            second.calculateStartPadding(direction)
        ),
        top = max(
            first.calculateTopPadding(),
            second.calculateTopPadding()
        ),
        end = max(
            first.calculateEndPadding(direction),
            second.calculateEndPadding(direction)
        ),
        bottom = max(
            first.calculateBottomPadding(),
            second.calculateBottomPadding()
        ),
    )
}
