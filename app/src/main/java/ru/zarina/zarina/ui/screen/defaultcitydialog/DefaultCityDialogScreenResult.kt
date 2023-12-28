package ru.zarina.zarina.ui.screen.defaultcitydialog

sealed class DefaultCityDialogScreenResult {
    data object ScreenClosed : DefaultCityDialogScreenResult()
}
