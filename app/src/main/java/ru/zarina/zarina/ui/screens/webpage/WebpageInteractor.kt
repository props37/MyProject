package ru.zarina.zarina.ui.screens.webpage

import ru.zarina.zarina.data.UserAgentHeaderProvider
import ru.zarina.zarina.usecase.authorization.GetAuthorizationTokenUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class WebpageInteractor @Inject constructor(
    private val userAgentHeaderProvider: UserAgentHeaderProvider,
    private val getAuthorizationTokenUseCase: GetAuthorizationTokenUseCase,
) {
    fun getUserAgentHeaders() = userAgentHeaderProvider.getHeaders()
    suspend fun getAuthorizationToken() = getAuthorizationTokenUseCase()
}
