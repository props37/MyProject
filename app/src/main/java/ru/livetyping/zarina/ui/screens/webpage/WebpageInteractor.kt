package ru.livetyping.zarina.ui.screens.webpage

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.UserAgentHeaderProvider
import ru.livetyping.zarina.usecase.old.authorization.GetAuthorizationTokenUseCase
import ru.livetyping.zarina.util.base.usecase.invoke

@Factory
class WebpageInteractor(
    private val userAgentHeaderProvider: UserAgentHeaderProvider,
    private val getAuthorizationTokenUseCase: GetAuthorizationTokenUseCase,
) {
    fun getUserAgentHeaders() = userAgentHeaderProvider.getHeaders()
    suspend fun getAuthorizationToken() = getAuthorizationTokenUseCase()
}
