package ru.livetyping.zarina.presentation.app

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val deps: AppDependencies,
) : ViewModel() {

    val startFeature: AppStartFeature = runBlocking {
        val isOnboardingCompleted = deps.getIsOnboardingCompletedFlow()
            .firstOrNull()?.getOrNull() ?: false
        if (isOnboardingCompleted) {
            AppStartFeature.HOME
        } else {
            AppStartFeature.ONBOARDING
        }
    }

    private val getWishlistProductIdsParams =
        GetWishlistProductIdsFlowUseCase.Params(CachePolicy.LocalOnly)

    val wishlistProductCount: StateFlow<Int> =
        deps.getWishlistProductIdsFlow(getWishlistProductIdsParams)
            .map { result ->
                result.getOrNull()?.size ?: 0
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileUiSubscribed,
                initialValue = 0,
            )

    val cartProductCount: StateFlow<Int> = deps.getCartProductCountFlow()
        .map { result ->
            result.getOrDefault(0)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = 0,
        )

    @OptIn(UnstableApi::class)
    val exoPlayerCache: Cache = deps.exoPlayerCache

    @OptIn(UnstableApi::class)
    val exoPlayerCacheDataSourceFactory: CacheDataSource.Factory =
        deps.exoPlayerCacheDataSourceFactory

    private val bearerTokensFlow = deps.getBearerTokensFlow()
        .map { it.getOrNull() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    init {
        fetchUser()
        fetchUserCity()
        fetchWishlistProductIdsOnBearerTokenChange()
        fetchCartProductIdsOnBearerTokenChange()
        performForcedSignOutOnRequests()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val params = GetUserFlowUseCase.Params(CachePolicy.Remote())
            deps.getUserFlow(params).firstOrNull()
        }
    }

    private fun fetchUserCity() {
        viewModelScope.launch {
            val params = GetUserCityFlowUseCase.Params(CachePolicy.Remote())
            deps.getUserCityFlow(params).firstOrNull()
        }
    }

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchWishlistProductIdsOnBearerTokenChange() {
        bearerTokensFlow
            .filterNotNull()
            .distinctUntilChanged()
            .transformLatest<BearerTokens, Unit> {
                // TODO: [High] Find a better way
                // Delay is used to prevent making requests with old authorization tokens
                // as tokens stored on the disk get updated earlier than HttpClient tokens
                delay(WISHLIST_PRODUCT_IDS_FETCHING_DELAY_MILLIS)
                val params = GetWishlistProductIdsFlowUseCase.Params(CachePolicy.Remote())
                deps.getWishlistProductIdsFlow(params).firstOrNull()
            }
            .launchIn(viewModelScope)
    }

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchCartProductIdsOnBearerTokenChange() {
        bearerTokensFlow
            .filterNotNull()
            .distinctUntilChanged()
            .transformLatest<BearerTokens, Unit> {
                // TODO: [High] Find a better way
                // Delay is used to prevent making requests with old authorization tokens
                // as tokens stored on the disk get updated earlier than HttpClient tokens
                delay(CART_PRODUCT_IDS_FETCHING_DELAY_MILLIS)
                val params = GetCartProductIdsFlowUseCase.Params(CachePolicy.Remote())
                deps.getCartProductIdsFlow(params).firstOrNull()
            }
            .launchIn(viewModelScope)
    }

    // TODO: [High] Do something with navigation?
    private fun performForcedSignOutOnRequests() {
        deps.getForcedSignOutRequestsFlow()
            .onEach { deps.forcedSignOut() }
            .launchIn(viewModelScope)
    }

    private companion object {
        private const val WISHLIST_PRODUCT_IDS_FETCHING_DELAY_MILLIS = 1000L
        private const val CART_PRODUCT_IDS_FETCHING_DELAY_MILLIS = 1000L
    }
}
