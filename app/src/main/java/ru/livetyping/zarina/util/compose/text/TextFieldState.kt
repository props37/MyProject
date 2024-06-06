package ru.livetyping.zarina.util.compose.text

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow

fun TextFieldState.textAsFlow(): Flow<CharSequence> = snapshotFlow { this.text }
