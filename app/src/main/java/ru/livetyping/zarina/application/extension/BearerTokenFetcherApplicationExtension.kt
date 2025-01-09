package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.core.domain.usecase.auth.FetchUnauthorizedUserBearerTokensUseCase
import javax.inject.Inject

class BearerTokenFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val fetchUnauthorizedUserBearerTokens: FetchUnauthorizedUserBearerTokensUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        coroutineScope.launch {
            fetchUnauthorizedUserBearerTokens()
        }
    }
}
