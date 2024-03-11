package ru.zarina.zarina.ui.screens.home

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.content.GetBannersUseCase
import ru.zarina.zarina.usecase.content.GetSelectionsUseCase
import ru.zarina.zarina.usecase.favorites.SetIsFavoriteUseCase
import ru.zarina.zarina.util.base.usecase.invoke

@Factory
class HomeInteractor(
    private val getBannersUseCase: GetBannersUseCase,
    private val getSelectionsUseCase: GetSelectionsUseCase,
    private val setIsFavoriteUseCase: SetIsFavoriteUseCase,
) {
    suspend fun getBanners() = getBannersUseCase()
    fun getSelections() = getSelectionsUseCase()
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean) =
        setIsFavoriteUseCase(SetIsFavoriteUseCase.Params(product, isFavorite))
}
