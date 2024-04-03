package ru.livetyping.zarina.ui.common.zarinasnack

import ru.livetyping.zarina.ui.base.text.Text

data class ZarinaSnackMessageButton(
    val text: Text,
    val onClick: () -> Unit,
)
