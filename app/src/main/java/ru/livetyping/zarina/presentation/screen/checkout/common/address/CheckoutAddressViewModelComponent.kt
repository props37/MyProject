package ru.livetyping.zarina.presentation.screen.checkout.common.address

import androidx.compose.foundation.text.input.TextFieldState
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.domain.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.domain.geography.Building
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.geography.Street
import ru.livetyping.zarina.domain.geography.exception.AddressNotFoundException
import ru.livetyping.zarina.presentation.base.viewmodel.ViewModelComponent
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.usecase.geography.GetCityStreetsFlowUseCase
import ru.livetyping.zarina.usecase.geography.GetStreetBuildingsFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(SavedStateHandleSaveableApi::class)
class CheckoutAddressViewModelComponent @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserCityFlow: GetUserCityFlowUseCase,
    private val getCityStreetsFlow: GetCityStreetsFlowUseCase,
    private val getStreetBuildingsFlow: GetStreetBuildingsFlowUseCase,
) : ViewModelComponent() {

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

    val searchApartmentTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val city: StateFlow<City?> = getUserCityFlow()
        .map { it.getOrNull() }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val streetsRequester = FlowRequester(AddressRequest.GENERAL) {
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

    private val streetsResult: StateFlow<Result<List<Street>>?> = streetsRequester.flow
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
            streets.map { Item.from(it) }
        }
        createState(resultItems, loadingState)
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = State.Loading,
    )

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
        val kladrId: KladrId,
        val name: String,
    ) {
        companion object {
            fun from(street: Street): Item {
                return Item(
                    kladrId = street.id,
                    name = street.name,
                )
            }

            fun from(building: Building): Item {
                return Item(
                    kladrId = building.id,
                    name = building.name,
                )
            }
        }
    }

    private enum class AddressRequest : FlowRequester.Request { GENERAL }
}
