package ru.livetyping.zarina.core.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import ru.livetyping.zarina.core.text.Text

@Composable
public fun textString(text: Text): String {
    // Will be recomposed when Configuration changes
    LocalConfiguration.current
    val context = LocalContext.current
    return text.getString(context)
}
