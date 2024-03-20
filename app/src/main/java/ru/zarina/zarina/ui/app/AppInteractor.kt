package ru.zarina.zarina.ui.app

import ru.zarina.zarina.usecase.cart.GetCartSizeFlowUseCase
import ru.zarina.zarina.usecase.device.GetIsOnboardingCompletedFlowUseCase
import ru.zarina.zarina.usecase.media.exoplayer.GetExoPlayerCacheDataSourceFactoryUseCase
import ru.zarina.zarina.usecase.media.exoplayer.GetExoPlayerCacheUseCase
import javax.inject.Inject

class AppInteractor @Inject constructor(
    val getIsOnboardingCompletedFlow: GetIsOnboardingCompletedFlowUseCase,
    val getCartSizeFlow: GetCartSizeFlowUseCase,
    val getExoPlayerCache: GetExoPlayerCacheUseCase,
    val getExoPlayerCacheDataSourceFactory: GetExoPlayerCacheDataSourceFactoryUseCase,
)
