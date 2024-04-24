package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import ru.livetyping.zarina.usecase.authorization.GetAuthorizationTokensFlowUseCase
import ru.livetyping.zarina.usecase.favorite.FetchFavoriteProductIdsUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class FavoriteProductIdsFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getAuthorizationTokensFlowUseCase: GetAuthorizationTokensFlowUseCase,
    private val fetchFavoriteProductIdsUseCase: FetchFavoriteProductIdsUseCase,
) : ApplicationExtension {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun install(application: Application) {
        getAuthorizationTokensFlowUseCase()
            .map { it.getOrNull() }
            .distinctUntilChanged()
            .transformLatest<AuthorizationTokens?, Unit> {
                // TODO: [High] Find a better way
                // Delay is used to prevent making requests with old authorization tokens
                // as tokens stored on the disk get updated earlier than HttpClient tokens
                delay(DELAY)
                if (it != null) {
                    fetchFavoriteProductIdsUseCase()
                }
            }
            .launchIn(coroutineScope)
    }

    companion object {
        private val DELAY: Duration get() = 500.milliseconds
    }
}
