package ru.zarina.zarina.utils.compose

import androidx.compose.ui.Modifier

fun Modifier.conditional(
    predicate: Boolean,
    operation: Modifier.() -> Modifier,
) = if (predicate) then(operation(this)) else this
