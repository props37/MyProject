package ru.livetyping.zarina.presentation.screen.shops

import android.Manifest
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.shop.Shop
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.datafetchinginfo.DataFetchingInfoHolder
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.packagename.PackageName
import ru.livetyping.zarina.presentation.common.permissionmanager.isGranted
import ru.livetyping.zarina.presentation.common.systemsettings.SystemSettings
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinasnack.ZarinaSnackMessage
import ru.livetyping.zarina.presentation.common.zarinasnack.ZarinaSnackMessageButton
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val interactor: ShopsInteractor,
) : ViewModel(), SideEffectSource<ShopsViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val permissionManager = interactor.permissionManager

    val viewModes: StateFlow<ImmutableList<ViewMode>> =
        MutableStateFlow(ViewMode.entries.toImmutableList()).asStateFlow()

    private val _currentViewMode = MutableStateFlow(ViewMode.MAP)
    val currentViewMode: StateFlow<ViewMode> = _currentViewMode.asStateFlow()

    private val currentLocationFetchingInfoHolder = DataFetchingInfoHolder<Unit>()
    private val shopsFetchingInfoHolder = DataFetchingInfoHolder<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentLocation: StateFlow<Location?> = currentLocationFetchingInfoHolder.fetchingRequests
        .flatMapLatest {
            interactor.getCurrentLocationFlow()
        }
        .map { result ->
            result.getOrNull()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val shopsResult: StateFlow<Result<List<Shop>>?> =
        shopsFetchingInfoHolder.fetchingRequests
            .flatMapLatest {
                interactor.getShopsFlow()
            }
            .onEach { shopsFetchingInfoHolder.completeFetching() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val shopListState: StateFlow<ShopListState> = combine(
        shopsResult,
        interactor.getUserCityFlow(),
        shopsFetchingInfoHolder.fetchingType,
    ) { shopsResult, userCityResult, fetchingType ->
        if (shopsResult == null || fetchingType != null) {
            ShopListState.Loading
        } else {
            shopsResult.fold(
                onSuccess = { shops ->
                    val userCity = userCityResult.getOrNull()
                    val cityShops = if (userCity != null) {
                        shops.filter { it.cityKladrId == userCity.kladrId }
                    } else {
                        shops
                    }
                    ShopListState.Success(cityShops.toImmutableList())
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
        currentLocationFetchingInfoHolder.requestFetching(Unit)
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

    fun onMyLocationClicked() {
        viewModelScope.launch {
            val fineLocationPermissionState =
                permissionManager.getPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
            if (fineLocationPermissionState.isGranted) {
                currentLocationFetchingInfoHolder.requestFetching(Unit)
            } else {
                val newPermissionsState =
                    permissionManager.requestMultiplePermissions(LOCATION_PERMISSIONS)
                if (newPermissionsState.any { it.value.isGranted }) {
                    currentLocationFetchingInfoHolder.requestFetching(Unit)
                } else {
                    val messageText = Text.Resource(R.string.current_location_missing_permission_error)
                    val button = ZarinaSnackMessageButton(
                        text = Text.Resource(R.string.to_settings),
                        onClick = {
                            val settings = SystemSettings.ApplicationDetails(PackageName.Own)
                            emitSideEffect(SideEffect.OpenSettings(settings))
                        },
                    )
                    val message = ZarinaSnackMessage(
                        text = messageText,
                        button = button,
                        duration = ZarinaSnackMessage.DURATION_LONG,
                    )
                    emitSideEffect(SideEffect.ShowZarinaSnack(message))
                }
            }
        }
    }

    fun onShopsErrorRefreshClicked() {
        shopsFetchingInfoHolder.requestFetching(Unit)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ShopsScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect

        data class ShowZarinaSnack(val message: ZarinaSnackMessage) : SideEffect

        data class OpenSettings(val settings: SystemSettings) : SideEffect
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

    companion object {
        private val LOCATION_PERMISSIONS: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
    }
}
