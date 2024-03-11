package ru.zarina.zarina.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.graph.HomeGraph
import ru.zarina.zarina.util.base.usecase.invoke
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val interactor: AppInteractor,
) : ViewModel() {

    val startDestination: Destination<Unit> = runBlocking {
        val isOnboardingCompleted = interactor.getIsOnboardingCompletedFlow()
            .firstOrNull()?.getOrNull() ?: false
        if (isOnboardingCompleted) {
            HomeGraph
        } else {
            UnscopedDestinations.Onboarding
        }
    }

    val cartProductCount: StateFlow<Int> = interactor.getCartProductCountFlow()
        .map { result ->
            result.getOrDefault(0)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = 0,
        )

    val exoPlayerCache: Cache = interactor.getExoPlayerCache()
    val exoPlayerCacheDataSourceFactory: CacheDataSource.Factory =
        interactor.getExoPlayerCacheDataSourceFactory()
}
