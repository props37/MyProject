package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.usecase.store.GetStoresFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.platform.PackageName
import ru.livetyping.zarina.core.platform.settings.SystemSettings
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.permission.PermissionRequiredDialogEvent
import ru.livetyping.zarina.core.uikit.permission.PermissionRequiredDialogState
import ru.livetyping.zarina.core.uikit.permission.RequiredPermission
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListViewMode
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class StoreListViewModel @Inject constructor(
    private val deps: StoreListDependencies,
) : ViewModel(), SideEffectSource<StoreListSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val viewModes = StoreListViewMode.getAll().toImmutableList()
    private val currentViewMode = MutableStateFlow(StoreListViewMode.MAP)

    val viewModeSelectorState: StateFlow<TabRowState<StoreListViewMode>> = currentViewMode.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { currentViewMode ->
        TabRowState(
            tabs = viewModes,
            currentTab = currentViewMode,
        )
    }

    private val storeRequester = FlowRequester(StoreRequest) {
        val params = GetStoresFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())
        deps.getStoresFlow(params)
    }

    private val storeResult = storeRequester.flow
        .conflate()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    val mapState: StateFlow<StoreListState> = combine(
        storeResult,
        storeRequester.loadingState,
    ) { storeResult, storeLoadingState ->
        createMapState(storeResult, storeLoadingState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = StoreListState.Loading,
    )

    private val userCityResultFlow =
        deps.getUserCityFlow(GetUserCityFlowUseCase.Params(CachePolicy.LocalFirstThenRemote()))

    val listState: StateFlow<StoreListState> = combine(
        storeResult,
        storeRequester.loadingState,
        userCityResultFlow,
    ) { storeResult, storeLoadingState, userCityResult ->
        createListState(storeResult, storeLoadingState, userCityResult)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = StoreListState.Loading,
    )

    private val currentLocationRequester = FlowRequester(LocationRequest) {
        deps.getCurrentLocationFlow()
    }

    val currentLocation = currentLocationRequester.flow
        .map { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = null,
        )

    private val _permissionRequiredDialogState =
        MutableStateFlow<PermissionRequiredDialogState>(PermissionRequiredDialogState.None)
    val permissionRequiredDialogState: StateFlow<PermissionRequiredDialogState> =
        _permissionRequiredDialogState.asStateFlow()

    fun onStoreListEvent(event: StoreListEvent) {
        when (event) {
            StoreListEvent.BackClicked -> onBackClicked()
            StoreListEvent.ErrorRefreshClicked -> storeRequester.request(StoreRequest)
            StoreListEvent.MyLocationClicked -> onMyLocationClicked()
        }
    }

    fun onViewModeSelectorEvent(event: TabRowEvent<StoreListViewMode>) {
        when (event) {
            is TabRowEvent.TabChanged -> currentViewMode.value = event.tab
            is TabRowEvent.TabReselected -> Unit
        }
    }

    fun onRequiredPermissionDialogEvent(event: PermissionRequiredDialogEvent) {
        when (event) {
            PermissionRequiredDialogEvent.CloseClicked -> {
                _permissionRequiredDialogState.value = PermissionRequiredDialogState.None
            }

            is PermissionRequiredDialogEvent.GoToSettingsClicked -> {
                if (event.permission == RequiredPermission.LOCATION) {
                    val systemSettings = SystemSettings.ApplicationDetails(PackageName.Own)
                    emitSideEffect(StoreListSideEffect.OpenSystemSettings(systemSettings))
                }
                _permissionRequiredDialogState.value = PermissionRequiredDialogState.None
            }
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = StoreListScreenAction.BackClicked
            emitSideEffect(StoreListSideEffect.Navigate(action))
        }
    }

    private fun onMyLocationClicked() {
        viewModelScope.launch {
            val fineLocationPermissionState =
                deps.permissionManager.getPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
            if (fineLocationPermissionState.isGranted) {
                currentLocationRequester.request(LocationRequest)
            } else {
                val newPermissionsState =
                    deps.permissionManager.requestMultiplePermissions(LOCATION_PERMISSIONS)
                if (newPermissionsState.any { it.value.isGranted }) {
                    currentLocationRequester.request(LocationRequest)
                } else {
                    _permissionRequiredDialogState.value = PermissionRequiredDialogState.Visible(
                        permission = RequiredPermission.LOCATION,
                        title = Text.Resource(RCommon.string.res_grant_location_permission),
                        body = Text.Resource(RCommon.string.res_it_will_help_us_to_detect_your_location),
                    )
                }
            }
        }
    }

    private fun createMapState(
        storeResult: Result<List<Store>>,
        storeLoadingState: FlowRequester.LoadingState,
    ): StoreListState {
        return if (storeLoadingState.isLoading()) {
            StoreListState.Loading
        } else {
            storeResult.fold(
                onSuccess = { stores ->
                    StoreListState.Success(stores.toImmutableList())
                },
                onFailure = {
                    val errorState = ZarinaErrorScreenState.from(it)
                    StoreListState.Error(errorState)
                },
            )
        }
    }

    private fun createListState(
        storeResult: Result<List<Store>>,
        storeLoadingState: FlowRequester.LoadingState,
        userCityResult: Result<City?>,
    ): StoreListState {
        return if (storeLoadingState.isLoading()) {
            StoreListState.Loading
        } else {
            storeResult.fold(
                onSuccess = { stores ->
                    val userCity = userCityResult.getOrNull()
                    val cityStores = if (userCity != null) {
                        stores.filter { it.city?.kladrId == userCity.id }
                    } else {
                        stores
                    }
                    StoreListState.Success(cityStores.toImmutableList())
                },
                onFailure = {
                    val errorState = ZarinaErrorScreenState.from(it)
                    StoreListState.Error(errorState)
                },
            )
        }
    }

    private data object StoreRequest : FlowRequest

    private data object LocationRequest : FlowRequest

    private companion object {
        private val LOCATION_PERMISSIONS: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
    }
}
