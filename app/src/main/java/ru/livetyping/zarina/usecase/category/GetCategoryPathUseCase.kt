package ru.livetyping.zarina.usecase.category

import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.category.CategoryRepository
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.category.CategoryPath
import javax.inject.Inject

class GetCategoryPathUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : UseCase<GetCategoryPathUseCase.Params, CategoryPath>() {
    override suspend fun execute(params: Params): CategoryPath {
        val path = categoryRepository.getCategoryPath(params.categoryId)
        return path ?: error("CategoryPath not found for category ${params.categoryId}")
    }

    data class Params(val categoryId: Category.Id)
}
