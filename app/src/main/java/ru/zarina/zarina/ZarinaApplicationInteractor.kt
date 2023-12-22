package ru.zarina.zarina

import ru.zarina.zarina.usecase.rework.authorization.UpdateUnauthorizedUserAuthorizationTokensUseCase
import javax.inject.Inject

class ZarinaApplicationInteractor @Inject constructor(
    val updateUnauthorizedUserAuthorizationTokens: UpdateUnauthorizedUserAuthorizationTokensUseCase,
)
