package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.usecase.auth.GetBearerTokensFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class FavoriteProductIdsFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getBearerTokensFlow: GetBearerTokensFlowUseCase,
    private val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
) : ApplicationExtension {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun install(application: Application) {
        getBearerTokensFlow()
            .map { it.getOrNull() }
            .distinctUntilChanged()
            .transformLatest<BearerTokens?, Unit> { tokens ->
                // TODO: [High] Find a better way
                // Delay is used to prevent making requests with old authorization tokens
                // as tokens stored on the disk get updated earlier than HttpClient tokens
                delay(DELAY)
                if (tokens != null) {
                    val params = GetWishlistProductIdsFlowUseCase.Params(CachePolicy.Remote())
                    getWishlistProductIdsFlow(params).firstOrNull()
                }
            }
            .launchIn(coroutineScope)
    }

    companion object {
        private val DELAY: Duration get() = 1.seconds
    }
}
