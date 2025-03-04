package ru.livetyping.zarina.core.uicomponent.sizeselector.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorEvent
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorState

public class SizeSelectorComponent(
    private val listener: Listener,
) {
    private val _sizeSelectorState = MutableStateFlow<SizeSelectorState>(SizeSelectorState.Hidden)
    public val sizeSelectorState: StateFlow<SizeSelectorState> = _sizeSelectorState.asStateFlow()

    public fun showSizeSelector(product: Product) {
        _sizeSelectorState.value = SizeSelectorState.Visible(product)
    }

    public fun hideSizeSelector() {
        _sizeSelectorState.value = SizeSelectorState.Hidden
    }

    public fun onEvent(event: SizeSelectorEvent) {
        when (event) {
            SizeSelectorEvent.DismissRequested -> hideSizeSelector()
            is SizeSelectorEvent.SizeSelected -> {
                hideSizeSelector()
                val product = event.product
                val offer = event.offer
                if (offer.isAvailable) {
                    listener.onProductSizeAvailable(product, offer)
                } else {
                    listener.onProductSizeNotAvailable(product, offer)
                }
            }
        }
    }

    public fun shouldShowSizeSelector(product: Product): Boolean {
        return product.offers.size > 1
    }

    public interface Listener {
        public fun onProductSizeAvailable(product: Product, offer: ProductOffer)

        public fun onProductSizeNotAvailable(product: Product, offer: ProductOffer)
    }
}
