package ru.zarina.zarina.ui.screens.webpage

import ru.zarina.zarina.data.UserAgentHeaderProvider
import javax.inject.Inject

class WebpageInteractor @Inject constructor(
    private val userAgentHeaderProvider: UserAgentHeaderProvider,
) {
    fun getUserAgentHeaders() = userAgentHeaderProvider.getHeaders()
}
