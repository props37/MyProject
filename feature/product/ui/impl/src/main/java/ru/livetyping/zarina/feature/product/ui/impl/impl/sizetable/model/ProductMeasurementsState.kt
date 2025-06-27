package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.model.product.ProductHeight
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurement
import ru.livetyping.zarina.core.domain.model.product.ProductSizeEn

@Immutable
internal data class ProductMeasurementsState(
    val sizes: ImmutableList<ProductSizeEn>,
    val selectedSize: ProductSizeEn?,
    val heights: ImmutableList<ProductHeight>?,
    val selectedHeight: ProductHeight?,
    val measurements: ImmutableList<ProductMeasurement>?,
    val modelInfo: ProductDetailed.ModelInfo?,
)
