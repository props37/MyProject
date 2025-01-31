package ru.livetyping.zarina.core.uikit.sizeselector

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.product.Product

@Stable
public sealed class SizeSelectorState {
    @Immutable
    public data class Visible(val product: Product) : SizeSelectorState()

    public data object Hidden : SizeSelectorState()
}
