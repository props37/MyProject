package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.text.Text

@Stable
internal sealed class GenericBottomSheetState {
    data class ButtonState(val text: Text)

    @Immutable
    data class Visible(
        val body: Text,
        val buttonState: ButtonState?,
    ) : GenericBottomSheetState()

    data object Hidden : GenericBottomSheetState()
}
