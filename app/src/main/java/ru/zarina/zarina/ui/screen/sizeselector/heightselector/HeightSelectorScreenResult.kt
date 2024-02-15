package ru.zarina.zarina.ui.screen.sizeselector.heightselector

sealed class HeightSelectorScreenResult {
    data object ScreenClosed : HeightSelectorScreenResult()

    data object SizeSelectorFlowClosed : HeightSelectorScreenResult()
}
