package ru.zarina.zarina

import ru.zarina.zarina.usecase.rework.authorization.FetchUnauthorizedUserAuthorizationTokensUseCase
import javax.inject.Inject

class ZarinaApplicationInteractor @Inject constructor(
    val fetchUnauthorizedUserAuthorizationTokensUseCase: FetchUnauthorizedUserAuthorizationTokensUseCase,
)
