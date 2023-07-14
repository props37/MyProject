package ru.zarina.zarina.ui.screens.home

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.content.GetBannersUseCase
import ru.zarina.zarina.utils.clean.invoke

@Factory
class HomeInteractor(
    private val getBannersUseCase: GetBannersUseCase,
) {
    suspend fun getBanners() = getBannersUseCase()
}
