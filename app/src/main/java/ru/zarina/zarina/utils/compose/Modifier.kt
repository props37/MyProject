package ru.zarina.zarina.utils.compose

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.conditional(
    predicate: Boolean,
    operation: Modifier.() -> Modifier,
) = if (predicate) then(operation(this)) else this

private val MinInteractibleSize = 48.dp
fun Modifier.minInteractionSize(): Modifier =
    this.sizeIn(minWidth = MinInteractibleSize, minHeight = MinInteractibleSize)
