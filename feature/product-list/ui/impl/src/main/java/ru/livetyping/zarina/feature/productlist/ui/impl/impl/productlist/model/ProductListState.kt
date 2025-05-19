package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort

@Stable
internal data class ProductListState(
    val categoryName: String?,
    val subcategoryListState: SubcategoryListState,
    val productPagingDataFlow: Flow<PagingData<ProductShort>>,
    val interceptSystemBack: Boolean,
)
