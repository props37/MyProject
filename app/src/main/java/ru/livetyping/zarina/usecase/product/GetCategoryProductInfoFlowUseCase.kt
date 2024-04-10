package ru.livetyping.zarina.usecase.product

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.product.ProductRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.CategoryProductInfo
import javax.inject.Inject

class GetCategoryProductInfoFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productRepository: ProductRepository,
) : FlowUseCase<GetCategoryProductInfoFlowUseCase.Params, CategoryProductInfo>(dispatcher) {

    override fun execute(params: Params): Flow<CategoryProductInfo> {
        return productRepository.getCategoryProductInfoFlow(
            categoryId = params.categoryId,
            filters = params.filters,
        )
    }

    data class Params(
        val categoryId: Category.Id,
        val filters: Filters?,
    )
}
