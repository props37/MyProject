package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model

import ru.livetyping.zarina.core.domain.model.product.ProductHeight
import ru.livetyping.zarina.core.domain.model.product.ProductSizeEn

internal sealed interface SizeTableEvent {
    data object CloseClicked : SizeTableEvent

    data class ViewModeSelected(val mode: ViewMode) : SizeTableEvent

    data class ProductMeasurementsSizeSelected(val size: ProductSizeEn) : SizeTableEvent

    data class ProductMeasurementsHeightSelected(val height: ProductHeight) : SizeTableEvent
}
