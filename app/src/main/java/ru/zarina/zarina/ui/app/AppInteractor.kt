package ru.zarina.zarina.ui.app

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import ru.zarina.zarina.usecase.cart.GetCartSizeFlowUseCase
import ru.zarina.zarina.usecase.device.GetIsOnboardingCompletedFlowUseCase
import javax.inject.Inject

@OptIn(UnstableApi::class)
class AppInteractor @Inject constructor(
    val getIsOnboardingCompletedFlow: GetIsOnboardingCompletedFlowUseCase,
    val getCartSizeFlow: GetCartSizeFlowUseCase,
    val exoPlayerCache: Cache,
    val exoPlayerCacheDataSourceFactory: CacheDataSource.Factory,
)
