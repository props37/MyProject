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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.Address
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.City
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

    val streetTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val buildingTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val apartmentTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val searchStreetTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val searchBuildingTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val isBuildingSelectionEnabled: Flow<Boolean> =
        selectedStreetValueHolder.stateFlow.map { it != null }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val streetsRequester = FlowRequester(AddressRequest) {
        val queryFlow = searchStreetTextFieldState
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
        val queryFlow = searchBuildingTextFieldState
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

    suspend fun getCurrentAddress(): Address? {
        val city = getCurrentCity()
        val street = getSelectedStreet()
        val building = getSelectedBuilding()
        return if (city != null && street != null && building != null) {
            Address(
                city = city,
                street = street,
                building = building,
                apartment = getCurrentApartment(),
            )
        } else {
            null
        }
    }

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
        streetTextFieldState.setTextAndPlaceCursorAtEnd(streetItem.address.name)
        searchStreetTextFieldState.setTextAndPlaceCursorAtEnd(streetItem.address.name)
    }

    fun onBuildingSelected(building: AddressSearchItem) {
        val parcelable = BuildingParcelable(
            id = building.address.id.value,
            name = building.address.name,
        )
        selectedBuildingValueHolder.set(parcelable)
        buildingTextFieldState.setTextAndPlaceCursorAtEnd(building.address.name)
        searchBuildingTextFieldState.setTextAndPlaceCursorAtEnd(building.address.name)
    }

    fun onStreetSearchErrorRefreshClicked() {
        streetsRequester.request(AddressRequest)
    }

    fun onBuildingSearchErrorRefreshClicked() {
        buildingsRequester.request(AddressRequest)
    }

    private suspend fun getCurrentCity(): City? {
        val params = GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly)
        return getUserCityFlowUseCase(params).firstOrNull()?.getOrNull()
    }

    private fun getSelectedStreet(): Street? {
        return selectedStreetValueHolder.get()?.toStreet()
    }

    private fun getSelectedBuilding(): Building? {
        return selectedBuildingValueHolder.get()?.toBuilding()
    }

    private fun getCurrentApartment(): String? {
        return apartmentTextFieldState.text.toString().takeIf { it.isNotBlank() }
    }

    private fun clearSelectedBuilding() {
        selectedBuildingValueHolder.set(null)
        buildingTextFieldState.setTextAndPlaceCursorAtEnd("")
        searchBuildingTextFieldState.setTextAndPlaceCursorAtEnd("")
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
