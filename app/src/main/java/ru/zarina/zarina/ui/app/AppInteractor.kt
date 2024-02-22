package ru.zarina.zarina.ui.app

import ru.zarina.zarina.usecase.rework.cart.GetCartProductCountFlowUseCase
import ru.zarina.zarina.usecase.rework.device.GetIsOnboardingCompletedFlowUseCase
import ru.zarina.zarina.usecase.rework.media.exoplayer.GetExoPlayerCacheDataSourceFactoryUseCase
import ru.zarina.zarina.usecase.rework.media.exoplayer.GetExoPlayerCacheUseCase
import javax.inject.Inject

class AppInteractor @Inject constructor(
    val getIsOnboardingCompletedFlow: GetIsOnboardingCompletedFlowUseCase,
    val getCartProductCountFlow: GetCartProductCountFlowUseCase,
    val getExoPlayerCache: GetExoPlayerCacheUseCase,
    val getExoPlayerCacheDataSourceFactory: GetExoPlayerCacheDataSourceFactoryUseCase,
)
