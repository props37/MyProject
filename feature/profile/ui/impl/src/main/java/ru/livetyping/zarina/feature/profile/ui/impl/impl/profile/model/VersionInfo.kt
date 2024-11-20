package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.text.Text

@Immutable
internal data class VersionInfo(
    val title: Text,
    val version: Text,
)
