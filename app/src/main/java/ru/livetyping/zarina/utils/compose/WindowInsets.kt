package ru.livetyping.zarina.utils.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable

@Deprecated("Use navigationBarsOrIme instead.")
val WindowInsets.Companion.navigationOrIme
    @Composable
    get() = ime.union(navigationBars)
