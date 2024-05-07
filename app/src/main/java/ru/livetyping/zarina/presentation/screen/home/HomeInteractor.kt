package ru.livetyping.zarina.presentation.screen.home

import ru.livetyping.zarina.usecase.content.GetHomeContentFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserContentGenderFlowUseCase
import ru.livetyping.zarina.usecase.user.SetUserContentGenderUseCase
import javax.inject.Inject

class HomeInteractor @Inject constructor(
    val getHomeContentFlow: GetHomeContentFlowUseCase,
    val getUserContentGenderFlow: GetUserContentGenderFlowUseCase,
    val setUserContentGender: SetUserContentGenderUseCase,
)
