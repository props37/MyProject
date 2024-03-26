package ru.zarina.zarina.ui.screen.home

import ru.zarina.zarina.usecase.content.GetHomeContentFlowUseCase
import ru.zarina.zarina.usecase.user.GetUserContentGenderFlowUseCase
import ru.zarina.zarina.usecase.user.SetUserContentGenderUseCase
import javax.inject.Inject

class HomeInteractor @Inject constructor(
    val getHomeContentFlow: GetHomeContentFlowUseCase,
    val getUserContentGenderFlow: GetUserContentGenderFlowUseCase,
    val setUserContentGender: SetUserContentGenderUseCase,
)
