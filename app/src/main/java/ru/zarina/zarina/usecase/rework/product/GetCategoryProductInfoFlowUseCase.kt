package ru.zarina.zarina.usecase.rework.product

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.product.ProductRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.product.CategoryProductInfo
import ru.zarina.zarina.usecase.base.FlowUseCase
import javax.inject.Inject

class GetCategoryProductInfoFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productRepository: ProductRepository,
) : FlowUseCase<GetCategoryProductInfoFlowUseCase.Params, CategoryProductInfo>(dispatcher) {

    override fun execute(params: Params): Flow<CategoryProductInfo> {
        return productRepository.getCategoryProductInfoFlow(params.categoryId)
    }

    data class Params(val categoryId: Category.Id)
}
