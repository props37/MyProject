package ru.zarina.zarina.ui.screen.home

import ru.zarina.zarina.usecase.rework.content.GetHomeBannersUseCase
import javax.inject.Inject

class HomeInteractor @Inject constructor(
    val getHomeBanners: GetHomeBannersUseCase,
)
