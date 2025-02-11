package ru.livetyping.zarina.core.uicompose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.LayoutDirection

@Stable
public fun PaddingValues.plus(
    other: PaddingValues,
    layoutDirection: LayoutDirection,
): PaddingValues {
    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
    )
}

@Stable
public fun PaddingValues.getVerticalPaddingValues(): PaddingValues {
    return PaddingValues(
        top = this.calculateTopPadding(),
        bottom = this.calculateBottomPadding(),
    )
}

@Stable
public fun PaddingValues.getHorizontalPaddingValues(
    layoutDirection: LayoutDirection,
): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(layoutDirection),
        end = this.calculateEndPadding(layoutDirection),
    )
}
