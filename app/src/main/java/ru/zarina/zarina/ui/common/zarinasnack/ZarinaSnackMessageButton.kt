package ru.zarina.zarina.ui.common.zarinasnack

import ru.zarina.zarina.ui.base.text.Text

data class ZarinaSnackMessageButton(
    val text: Text,
    val onClick: () -> Unit,
)
