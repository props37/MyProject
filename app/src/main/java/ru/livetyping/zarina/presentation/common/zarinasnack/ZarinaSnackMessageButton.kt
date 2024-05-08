package ru.livetyping.zarina.presentation.common.zarinasnack

import ru.livetyping.zarina.presentation.base.text.Text

data class ZarinaSnackMessageButton(
    val text: Text,
    val onClick: () -> Unit,
    val removeSnackOnClick: Boolean = true,
)
