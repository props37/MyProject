package ru.livetyping.zarina.presentation.screen.stores

import android.Manifest
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.permissionmanager.isGranted
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.screen.stores.StoresViewModel.SideEffect
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class StoresViewModel @Inject constructor(
    private val interactor: StoresInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val permissionManager = interactor.permissionManager

    val viewModes: StateFlow<ImmutableList<ViewMode>> =
        MutableStateFlow(ViewMode.entries.toImmutableList()).asStateFlow()

    private val _currentViewMode = MutableStateFlow(ViewMode.MAP)
    val currentViewMode: StateFlow<ViewMode> = _currentViewMode.asStateFlow()

    private val currentLocationRequester = FlowRequester(LocationRequest.GENERAL) {
        interactor.getCurrentLocationFlow()
    }

    private val storesRequester = FlowRequester(StoresRequest.GENERAL) {
        interactor.getStoresFlow()
    }

    val currentLocation: StateFlow<Location?> = currentLocationRequester.flow
        .map { result ->
            result.getOrNull()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    private val storesResult: StateFlow<Result<List<Store>>?> = storesRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val storeMapState: StateFlow<StoreListState> = combine(
        storesResult,
        storesRequester.loadingState,
    ) { storesResult, loadingState->
        if (storesResult == null || loadingState.isLoading) {
            StoreListState.Loading
        } else {
            storesResult.fold(
                onSuccess = {
                    StoreListState.Success(it.toImmutableList())
                },
                onFailure = {
                    val errorState = ErrorState.from(it)
                    StoreListState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = StoreListState.Loading,
    )

    val storeListState: StateFlow<StoreListState> = combine(
        storesResult,
        interactor.getUserCityFlow(),
        storesRequester.loadingState,
    ) { storesResult, userCityResult, loadingState ->
        if (storesResult == null || loadingState.isLoading) {
            StoreListState.Loading
        } else {
            storesResult.fold(
                onSuccess = { stores ->
                    val userCity = userCityResult.getOrNull()
                    val cityStores = if (userCity != null) {
                        stores.filter { it.cityKladrId == userCity.kladrId }
                    } else {
                        stores
                    }
                    StoreListState.Success(cityStores.toImmutableList())
                },
                onFailure = {
                    val errorState = ErrorState.from(it)
                    StoreListState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = StoreListState.Loading,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = StoresScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onViewModeChanged(mode: ViewMode) {
        _currentViewMode.value = mode
    }

    fun onMyLocationClicked() {
        viewModelScope.launch {
            val fineLocationPermissionState =
                permissionManager.getPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
            if (fineLocationPermissionState.isGranted) {
                currentLocationRequester.request(LocationRequest.GENERAL)
            } else {
                val newPermissionsState =
                    permissionManager.requestMultiplePermissions(LOCATION_PERMISSIONS)
                if (newPermissionsState.any { it.value.isGranted }) {
                    currentLocationRequester.request(LocationRequest.GENERAL)
                } else {
                    val action = StoresScreenAction.LocationPermissionRequired
                    emitSideEffect(SideEffect.Navigate(action))
                }
            }
        }
    }

    fun onStoresErrorRefreshClicked() {
        storesRequester.request(StoresRequest.GENERAL)
    }

    fun onStoreClicked(store: Store) {
        navigationThrottler.throttle {
            val action = StoresScreenAction.StoreClicked(store)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: StoresScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    enum class ViewMode { MAP, LIST }

    @Stable
    sealed class StoreListState {

        @Immutable
        data class Success(val stores: ImmutableList<Store>) : StoreListState()

        data object Loading : StoreListState()

        @Immutable
        data class Error(val state: ErrorState) : StoreListState()
    }

    private enum class LocationRequest : FlowRequester.Request { GENERAL }

    private enum class StoresRequest : FlowRequester.Request { GENERAL }

    companion object {
        private val LOCATION_PERMISSIONS: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
    }
}
