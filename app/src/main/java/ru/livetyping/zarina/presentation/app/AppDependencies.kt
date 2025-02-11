package ru.livetyping.zarina.presentation.app

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductCountFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.onboarding.GetIsOnboardingCompletedFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.ForcedSignOutUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetForcedSignOutRequestsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import javax.inject.Inject

@OptIn(UnstableApi::class)
class AppDependencies @Inject constructor(
    val getIsOnboardingCompletedFlow: GetIsOnboardingCompletedFlowUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val getCartProductCountFlow: GetCartProductCountFlowUseCase,
    val getForcedSignOutRequestsFlow: GetForcedSignOutRequestsFlowUseCase,
    val forcedSignOut: ForcedSignOutUseCase,
    val exoPlayerCache: Cache,
    val exoPlayerCacheDataSourceFactory: CacheDataSource.Factory,
)
