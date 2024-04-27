package ru.livetyping.zarina.ui.app

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import ru.livetyping.zarina.usecase.cart.GetCartSizeFlowUseCase
import ru.livetyping.zarina.usecase.device.GetIsOnboardingCompletedFlowUseCase
import ru.livetyping.zarina.usecase.user.ForcedSignOutUseCase
import ru.livetyping.zarina.usecase.user.GetForcedSignOutRequestFlowUseCase
import javax.inject.Inject

@OptIn(UnstableApi::class)
class AppInteractor @Inject constructor(
    val getIsOnboardingCompletedFlow: GetIsOnboardingCompletedFlowUseCase,
    val getCartSizeFlow: GetCartSizeFlowUseCase,
    val exoPlayerCache: Cache,
    val exoPlayerCacheDataSourceFactory: CacheDataSource.Factory,
    val getForcedSignOutRequestFlow: GetForcedSignOutRequestFlowUseCase,
    val forcedSignOut: ForcedSignOutUseCase,
)
