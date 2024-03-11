package ru.zarina.zarina.ui.screens.webpage

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.UserAgentHeaderProvider
import ru.zarina.zarina.usecase.authorization.GetAuthorizationTokenUseCase
import ru.zarina.zarina.util.base.usecase.invoke

@Factory
class WebpageInteractor(
    private val userAgentHeaderProvider: UserAgentHeaderProvider,
    private val getAuthorizationTokenUseCase: GetAuthorizationTokenUseCase,
) {
    fun getUserAgentHeaders() = userAgentHeaderProvider.getHeaders()
    suspend fun getAuthorizationToken() = getAuthorizationTokenUseCase()
}
