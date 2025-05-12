package ru.livetyping.zarina.core.uikit.toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastController2
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2

// TODO: [Top] Remove after full migration

public val LocalZarinaToastController2: ProvidableCompositionLocal<ZarinaToastController2> =
    staticCompositionLocalOf { NoOpZarinaToastController2() }

@Composable
public fun rememberZarinaToastController2(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ZarinaToastController2 {
    return remember(coroutineScope) { ZarinaToastController2Impl(coroutineScope) }
}

internal class NoOpZarinaToastController2 : ZarinaToastController2 {
    override val currentMessage: StateFlow<ZarinaToastMessage2?>
        get() = throw NotImplementedError()

    override fun show(message: ZarinaToastMessage2, removePreviousMessage: Boolean) {
        throw NotImplementedError()
    }

    override fun cancelCurrentToast() {
        throw NotImplementedError()
    }
}
