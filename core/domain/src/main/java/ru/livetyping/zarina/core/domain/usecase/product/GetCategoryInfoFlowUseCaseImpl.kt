package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.CategoryInfo
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetCategoryInfoFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCategoryInfoFlowUseCaseImpl(
    private val productRepository: ProductRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, CategoryInfo>(logger), GetCategoryInfoFlowUseCase {

    override fun execute(params: Params): Flow<CategoryInfo> {
        return productRepository.getCategoryInfoFlow(params.categoryId, params.filters)
    }

    override fun invoke(params: Params): Flow<Result<CategoryInfo>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCategoryInfoFlowUseCaseImpl"
    }
}
