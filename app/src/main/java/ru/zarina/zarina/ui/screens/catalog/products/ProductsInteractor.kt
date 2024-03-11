package ru.zarina.zarina.ui.screens.catalog.products

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.category.ICategoryRepository
import ru.zarina.zarina.domain.old.Category
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.usecase.old.catalog.GetProductsPageUseCase
import ru.zarina.zarina.usecase.old.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.usecase.old.favorites.SetIsFavoriteUseCase
import ru.zarina.zarina.util.base.usecase.invoke

@Factory
class ProductsInteractor(
    private val categoryRepository: ICategoryRepository,
    val getProductsPageUseCase: GetProductsPageUseCase,
    val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val setIsFavoriteUseCase: SetIsFavoriteUseCase,
) {
    fun getCategory(id: Category.Id) = categoryRepository.getCategory(id)
    fun getFavoriteIds() = getFavoriteIdsUseCase()
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean) =
        setIsFavoriteUseCase(SetIsFavoriteUseCase.Params(product, isFavorite))
}
