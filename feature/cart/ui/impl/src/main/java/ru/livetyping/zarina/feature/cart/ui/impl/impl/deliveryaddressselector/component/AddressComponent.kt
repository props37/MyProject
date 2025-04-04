package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.component

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.Address
import ru.livetyping.zarina.core.domain.model.geo.Street
import ru.livetyping.zarina.core.domain.usecase.geo.GetCityStreetsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.geo.GetStreetBuildingsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uimodel.geo.BuildingParcelable
import ru.livetyping.zarina.core.uimodel.geo.StreetParcelable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.AddressSearchItem
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.AddressSearchState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.AddressSearchStateBuilder

@OptIn(SavedStateHandleSaveableApi::class)
internal class AddressComponent(
    savedStateHandle: SavedStateHandle,
    coroutineScope: CoroutineScope,
    private val getUserCityFlowUseCase: GetUserCityFlowUseCase,
    private val getCityStreetsFlowUseCase: GetCityStreetsFlowUseCase,
    private val getStreetBuildingsFlowUseCase: GetStreetBuildingsFlowUseCase,
) {
    private val selectedStreetValueHolder = savedStateHandle.createValueHolder<StreetParcelable?>(
        key = Keys.SELECTED_STREET.key,
        initialValue = null,
    )

    private val selectedBuildingValueHolder =
        savedStateHandle.createValueHolder<BuildingParcelable?>(
            key = Keys.SELECTED_BUILDING.key,
            initialValue = null,
        )

    val streetSelectorTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val buildingSelectorTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val apartmentSelectorTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val streetSearchTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val buildingSearchTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val isBuildingSelectionEnabled: Flow<Boolean> =
        selectedStreetValueHolder.stateFlow.map { it != null }

    private val getCityUseCaseParams = GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly)
    private val cityFlow = getUserCityFlowUseCase(getCityUseCaseParams).map { it.getOrNull() }

    val currentAddress = combine(
        cityFlow,
        selectedStreetValueHolder.stateFlow,
        selectedBuildingValueHolder.stateFlow,
        apartmentSelectorTextFieldState.textAsFlow(),
    ) { city, streetParcelable, buildingParcelable, apartment ->
        if (city != null && streetParcelable != null && buildingParcelable != null) {
            Address(
                city = city,
                street = streetParcelable.toStreet(),
                building = buildingParcelable.toBuilding(),
                apartment = apartment.toString(),
            )
        } else {
            null
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val streetsRequester = FlowRequester(AddressRequest) {
        val queryFlow = streetSearchTextFieldState
            .textAsFlow()
            .debounce(SEARCH_QUERY_DEBOUNCE_MILLIS)
        queryFlow.flatMapLatest { query ->
            val params = GetCityStreetsFlowUseCase.Params(query.toString())
            getCityStreetsFlowUseCase(params)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val buildingsRequester = FlowRequester(AddressRequest) {
        val streetParcelableFlow = selectedStreetValueHolder.stateFlow
        val queryFlow = buildingSearchTextFieldState
            .textAsFlow()
            .debounce(SEARCH_QUERY_DEBOUNCE_MILLIS)
        combine(streetParcelableFlow, queryFlow) { streetParcelable, query ->
            val street = streetParcelable?.toStreet()
            if (street != null) {
                val params = GetStreetBuildingsFlowUseCase.Params(
                    streetKladrId = street.id,
                    nameQuery = query.toString(),
                )
                getStreetBuildingsFlowUseCase(params)
            } else {
                emptyFlow()
            }
        }.flatMapLatest { it }
    }

    private val addressSearchStateBuilder = AddressSearchStateBuilder()

    val streetSearchState: StateFlow<AddressSearchState> = combine(
        streetsRequester.flow,
        streetsRequester.loadingState,
    ) { result, loadingState ->
        addressSearchStateBuilder.build(result, loadingState)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = AddressSearchState.Empty,
    )

    val buildingSearchState: StateFlow<AddressSearchState> = combine(
        buildingsRequester.flow,
        buildingsRequester.loadingState,
    ) { result, loadingState ->
        addressSearchStateBuilder.build(result, loadingState)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = AddressSearchState.Empty,
    )

    fun onStreetSelected(streetItem: AddressSearchItem) {
        val currentStreet = getSelectedStreet()
        if (streetItem.address.id != currentStreet?.id) {
            clearSelectedBuilding()
        }

        val parcelable = StreetParcelable(
            id = streetItem.address.id.value,
            name = streetItem.address.name,
        )
        selectedStreetValueHolder.set(parcelable)
        streetSelectorTextFieldState.setTextAndPlaceCursorAtEnd(streetItem.address.name)
        streetSearchTextFieldState.setTextAndPlaceCursorAtEnd(streetItem.address.name)
    }

    fun onBuildingSelected(building: AddressSearchItem) {
        val parcelable = BuildingParcelable(
            id = building.address.id.value,
            name = building.address.name,
        )
        selectedBuildingValueHolder.set(parcelable)
        buildingSelectorTextFieldState.setTextAndPlaceCursorAtEnd(building.address.name)
        buildingSearchTextFieldState.setTextAndPlaceCursorAtEnd(building.address.name)
    }

    fun onStreetSearchErrorRefreshClicked() {
        streetsRequester.request(AddressRequest)
    }

    fun onBuildingSearchErrorRefreshClicked() {
        buildingsRequester.request(AddressRequest)
    }

    private fun getSelectedStreet(): Street? {
        return selectedStreetValueHolder.get()?.toStreet()
    }

    private fun clearSelectedBuilding() {
        selectedBuildingValueHolder.set(null)
        buildingSelectorTextFieldState.setTextAndPlaceCursorAtEnd("")
        buildingSearchTextFieldState.setTextAndPlaceCursorAtEnd("")
    }

    private data object AddressRequest : FlowRequest

    private enum class Keys {
        SELECTED_STREET,
        SELECTED_BUILDING;

        val key: String get() = name
    }

    private companion object {
        private const val SEARCH_QUERY_DEBOUNCE_MILLIS = 300L
    }
}
