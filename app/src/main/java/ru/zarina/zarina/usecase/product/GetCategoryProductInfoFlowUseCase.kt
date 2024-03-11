package ru.zarina.zarina.usecase.product

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.product.ProductRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.rework.filter.Filters
import ru.zarina.zarina.domain.rework.product.CategoryProductInfo
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
