package ru.zarina.zarina.ui.screens.catalog.products

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.category.ICategoryRepository
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.catalog.GetProductsPageUseCase
import ru.zarina.zarina.usecase.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.usecase.favorites.SetIsFavoriteUseCase
import ru.zarina.zarina.utils.clean.invoke

@Factory
class ProductsInteractor(
    private val categoryRepository: ICategoryRepository,
    val getProductsPageUseCase: GetProductsPageUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val setIsFavoriteUseCase: SetIsFavoriteUseCase,
) {
    fun getCategory(id: Category.Id) = categoryRepository.getCategory(id)
    fun getFavoriteIds() = getFavoriteIdsUseCase()
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean) =
        setIsFavoriteUseCase(SetIsFavoriteUseCase.Params(product, isFavorite))
}
