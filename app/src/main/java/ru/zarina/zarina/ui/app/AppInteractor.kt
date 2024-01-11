package ru.zarina.zarina.ui.app

import ru.zarina.zarina.usecase.rework.device.GetIsOnboardingCompletedUseCase
import ru.zarina.zarina.usecase.rework.media.exoplayer.GetExoPlayerCacheDataSourceFactoryUseCase
import ru.zarina.zarina.usecase.rework.media.exoplayer.GetExoPlayerCacheUseCase
import javax.inject.Inject

class AppInteractor @Inject constructor(
    val getIsOnboardingCompleted: GetIsOnboardingCompletedUseCase,
    val getExoPlayerCache: GetExoPlayerCacheUseCase,
    val getExoPlayerCacheDataSourceFactory: GetExoPlayerCacheDataSourceFactoryUseCase,
)
