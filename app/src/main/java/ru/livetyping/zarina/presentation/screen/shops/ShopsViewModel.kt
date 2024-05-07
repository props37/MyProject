package ru.livetyping.zarina.presentation.screen.shops

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.shop.Shop
import ru.livetyping.zarina.presentation.common.datafetchinginfo.DataFetchingInfoHolder
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val interactor: ShopsInteractor,
) : ViewModel(), SideEffectSource<ShopsViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val viewModes: StateFlow<ImmutableList<ViewMode>> =
        MutableStateFlow(ViewMode.entries.toImmutableList()).asStateFlow()

    private val _currentViewMode = MutableStateFlow(ViewMode.MAP)
    val currentViewMode: StateFlow<ViewMode> = _currentViewMode.asStateFlow()

    private val shopsFetchingInfoHolder = DataFetchingInfoHolder<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val userCityShopsResult: StateFlow<Result<List<Shop>>?> =
        shopsFetchingInfoHolder.fetchingRequests
            .flatMapLatest {
                interactor.getUserCityShopsFlow()
            }
            .onEach { shopsFetchingInfoHolder.completeFetching() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val shopListState: StateFlow<ShopListState> = combine(
        userCityShopsResult,
        shopsFetchingInfoHolder.fetchingType,
    ) { shopsResult, fetchingType ->
        if (shopsResult == null || fetchingType != null) {
            ShopListState.Loading
        } else {
            shopsResult.fold(
                onSuccess = {
                    ShopListState.Success(it.toImmutableList())
                },
                onFailure = {
                    val errorState = ErrorState.from(it)
                    ShopListState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = ShopListState.Loading,
    )

    init {
        shopsFetchingInfoHolder.requestFetching(Unit)
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ShopsScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onViewModeChanged(mode: ViewMode) {
        _currentViewMode.value = mode
    }

    fun onShopsErrorRefreshClicked() {
        shopsFetchingInfoHolder.requestFetching(Unit)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ShopsScreenAction) : SideEffect
    }

    enum class ViewMode { MAP, LIST }

    @Stable
    sealed class ShopListState {

        @Immutable
        data class Success(val shops: ImmutableList<Shop>) : ShopListState()

        data object Loading : ShopListState()

        @Immutable
        data class Error(val state: ErrorState) : ShopListState()
    }
}
