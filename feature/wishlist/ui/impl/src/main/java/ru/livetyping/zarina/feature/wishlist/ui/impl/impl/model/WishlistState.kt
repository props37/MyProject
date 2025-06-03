package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.model

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort

@Stable
internal data class WishlistState(
    val productCount: Int?,
    val productPagingDataFlow: Flow<PagingData<ProductShort>>,
)
