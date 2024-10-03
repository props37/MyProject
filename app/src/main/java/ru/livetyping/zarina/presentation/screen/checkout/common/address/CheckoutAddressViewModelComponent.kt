package ru.livetyping.zarina.presentation.screen.checkout.common.address

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.domain.checkout.CheckoutAddress
import ru.livetyping.zarina.domain.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.domain.geography.AddressPart
import ru.livetyping.zarina.domain.geography.Building
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.Street
import ru.livetyping.zarina.domain.geography.exception.AddressNotFoundException
import ru.livetyping.zarina.presentation.base.viewmodel.ViewModelComponent
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.model.geography.BuildingParcelable
import ru.livetyping.zarina.presentation.model.geography.StreetParcelable
import ru.livetyping.zarina.usecase.geography.GetCityStreetsFlowUseCase
import ru.livetyping.zarina.usecase.geography.GetStreetBuildingsFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(SavedStateHandleSaveableApi::class)
class CheckoutAddressViewModelComponent @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserCityFlow: GetUserCityFlowUseCase,
    private val getCityStreetsFlow: GetCityStreetsFlowUseCase,
    private val getStreetBuildingsFlow: GetStreetBuildingsFlowUseCase,
) : ViewModelComponent() {

    private val selectedStreetValueHolder = savedStateHandle.createValueHolder<StreetParcelable?>(
        key = KEY_SELECTED_STREET,
        initialValue = null,
    )

    private val selectedBuildingValueHolder =
        savedStateHandle.createValueHolder<BuildingParcelable?>(
            key = KEY_SELECTED_BUILDING,
            initialValue = null,
        )

    private val city: StateFlow<City?> = getUserCityFlow()
        .map { it.getOrNull() }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val streetsRequester = FlowRequester(AddressRequest) {
        val queryFlow = searchStreetTextFieldState
            .textAsFlow()
            .debounce(300.milliseconds)
        combine(city, queryFlow) { city, query ->
            val params = GetCityStreetsFlowUseCase.Params(
                cityKladrId = city?.id ?: City.DEFAULT.id,
                nameQuery = query.toString(),
            )
            getCityStreetsFlow(params)
        }
            .flatMapLatest { it }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val buildingsRequester = FlowRequester(AddressRequest) {
        val queryFlow = searchBuildingTextFieldState
            .textAsFlow()
            .debounce(300.milliseconds)
        combine(selectedStreet, queryFlow) { street, query ->
            if (street != null) {
                val params = GetStreetBuildingsFlowUseCase.Params(
                    streetKladrId = street.id,
                    nameQuery = query.toString(),
                )
                getStreetBuildingsFlow(params)
            } else {
                emptyFlow()
            }
        }
            .flatMapLatest { it }
    }

    private val streetsResult: StateFlow<Result<List<Street>>?> = streetsRequester.flow
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val buildingsResult: StateFlow<Result<List<Building>>?> = buildingsRequester.flow
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val streetsState: StateFlow<State> = combine(
        streetsResult,
        streetsRequester.loadingState,
    ) { result, loadingState ->
        val resultItems = result?.map { streets ->
            streets.map { Item(it) }
        }
        createState(resultItems, loadingState)
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = State.Loading,
    )

    val buildingsState: StateFlow<State> = combine(
        buildingsResult,
        buildingsRequester.loadingState,
    ) { result, loadingState ->
        val resultItems = result?.map { buildings ->
            buildings.map { Item(it) }
        }
        createState(resultItems, loadingState)
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = State.Loading,
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

    val isBuildingSelectionEnabled: StateFlow<Boolean> = selectedStreetValueHolder.stateFlow
        .mapState(
            scope = scope,
            started = SharingStarted.WhileUiSubscribed,
        ) {
            it != null
        }

    val selectedStreet: StateFlow<Street?> = selectedStreetValueHolder.stateFlow.mapState(
        scope = scope,
        started = SharingStarted.Eagerly,
    ) {
        it?.toStreet()
    }

    val selectedBuilding: StateFlow<Building?> = selectedBuildingValueHolder.stateFlow.mapState(
        scope = scope,
        started = SharingStarted.Eagerly,
    ) {
        it?.toBuilding()
    }

    val selectedApartment: StateFlow<String?> = apartmentTextFieldState.textAsFlow()
        .map { str -> str.toString().takeIf { it.isNotBlank() } }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    fun getAddress(): CheckoutAddress? {
        val city = city.value
        val street = selectedStreet.value
        val building = selectedBuilding.value
        return if (city != null && street != null && building != null) {
            CheckoutAddress(
                city = city,
                street = street,
                building = building,
                apartment = selectedApartment.value,
            )
        } else null
    }

    fun onStreetSelected(street: Item) {
        if (street.addressPart.id != selectedStreet.value?.id) {
            clearSelectedBuilding()
        }

        val parcelable = StreetParcelable(
            id = street.addressPart.id.value,
            name = street.addressPart.name,
        )
        selectedStreetValueHolder.set(parcelable)
        streetTextFieldState.setTextAndPlaceCursorAtEnd(street.addressPart.name)
        searchStreetTextFieldState.setTextAndPlaceCursorAtEnd(street.addressPart.name)
    }

    fun onBuildingSelected(building: Item) {
        val parcelable = BuildingParcelable(
            id = building.addressPart.id.value,
            name = building.addressPart.name,
        )
        selectedBuildingValueHolder.set(parcelable)
        buildingTextFieldState.setTextAndPlaceCursorAtEnd(building.addressPart.name)
        searchBuildingTextFieldState.setTextAndPlaceCursorAtEnd(building.addressPart.name)
    }

    fun onStreetsErrorRefreshClicked() {
        streetsRequester.request(AddressRequest)
    }

    fun onBuildingsErrorRefreshClicked() {
        buildingsRequester.request(AddressRequest)
    }

    private fun clearSelectedBuilding() {
        selectedBuildingValueHolder.set(null)
        buildingTextFieldState.setTextAndPlaceCursorAtEnd("")
        searchBuildingTextFieldState.setTextAndPlaceCursorAtEnd("")
    }

    private fun createState(
        result: Result<List<Item>>?,
        loadingState: FlowRequester.LoadingState,
    ): State {
        return if (result == null || loadingState.isLoading()) {
            State.Loading
        } else {
            result.fold(
                onSuccess = { State.Items(it) },
                onFailure = {
                    when (it) {
                        is EmptySearchQueryException -> State.Empty
                        is AddressNotFoundException -> State.Empty
                        else -> {
                            val state = ErrorState.from(it)
                            State.Error(state)
                        }
                    }
                }
            )
        }
    }

    @Stable
    sealed class State {
        data object Loading : State()

        @Immutable
        data class Items(val items: List<Item>) : State()

        data object Empty : State()

        @Immutable
        data class Error(val state: ErrorState) : State()
    }

    @Immutable
    data class Item(
        val addressPart: AddressPart,
    )

    private data object AddressRequest : FlowRequester.Request

    companion object {
        private const val KEY_SELECTED_STREET = "selected_street"
        private const val KEY_SELECTED_BUILDING = "selected_building"
    }
}
