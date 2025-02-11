package ru.livetyping.zarina.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val deps: MainDependencies,
) : ViewModel() {

    private val bearerTokensFlow = deps.getBearerTokensFlow()
        .map { it.getOrNull() }
        .conflate()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    fun onScreenCreated() {
        fetchUser()
        fetchUserCity()
        fetchWishlistProductIdsOnBearerTokenChange()
        fetchCartProductIdsOnBearerTokenChange()
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

    @OptIn(ExperimentalCoroutinesApi::class)
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

    @OptIn(ExperimentalCoroutinesApi::class)
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

    private companion object {
        private const val WISHLIST_PRODUCT_IDS_FETCHING_DELAY_MILLIS = 1000L
        private const val CART_PRODUCT_IDS_FETCHING_DELAY_MILLIS = 1000L
    }
}
