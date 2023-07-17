package ru.zarina.zarina.ui.screens.home

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.content.GetBannersUseCase
import ru.zarina.zarina.usecase.content.GetSelectionsUseCase
import ru.zarina.zarina.utils.clean.invoke

@Factory
class HomeInteractor(
    private val getBannersUseCase: GetBannersUseCase,
    private val getSelectionsUseCase: GetSelectionsUseCase
) {
    suspend fun getBanners() = getBannersUseCase()
    suspend fun getSelections() = getSelectionsUseCase()
}
