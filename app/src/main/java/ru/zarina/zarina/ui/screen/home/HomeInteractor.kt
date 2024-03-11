package ru.zarina.zarina.ui.screen.home

import ru.zarina.zarina.usecase.content.GetHomeContentFlowUseCase
import javax.inject.Inject

class HomeInteractor @Inject constructor(
    val getHomeContentFlow: GetHomeContentFlowUseCase,
)
