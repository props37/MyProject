package ru.livetyping.zarina.presentation.app

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.usecase.user.ForcedSignOutUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetForcedSignOutRequestsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val interactor: AppInteractor,
    private val getForcedSignOutRequestsFlow: GetForcedSignOutRequestsFlowUseCase,
    private val forcedSignOut: ForcedSignOutUseCase,
) : ViewModel() {

    val startFeature: AppStartFeature = runBlocking {
        val isOnboardingCompleted = interactor.getIsOnboardingCompletedFlow()
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
        interactor.getWishlistProductIdsFlow(getWishlistProductIdsParams)
            .map { result ->
                result.getOrNull()?.size ?: 0
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileUiSubscribed,
                initialValue = 0,
            )

    // TODO: [Top] Migrate to new components
    val cartProductCount: StateFlow<Int> = interactor.getCartProductCountFlow()
        .map { result ->
            result.getOrDefault(0)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = 0,
        )

    @OptIn(UnstableApi::class)
    val exoPlayerCache: Cache = interactor.exoPlayerCache

    @OptIn(UnstableApi::class)
    val exoPlayerCacheDataSourceFactory: CacheDataSource.Factory =
        interactor.exoPlayerCacheDataSourceFactory

    init {
        listenToForcedSignOutRequests()
    }

    // TODO: [High] Do something with navigation?
    private fun listenToForcedSignOutRequests() {
        getForcedSignOutRequestsFlow()
            .onEach { forcedSignOut() }
            .launchIn(viewModelScope)
    }
}
