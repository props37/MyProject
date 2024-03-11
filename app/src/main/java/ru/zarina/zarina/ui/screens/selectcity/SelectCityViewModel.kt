package ru.zarina.zarina.ui.screens.selectcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.base.operationtracker.OperationKey
import ru.zarina.zarina.base.operationtracker.OperationTracker
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.MessageQueue
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.screens.bases.selectcity.SelectCityComponent
import ru.zarina.zarina.utils.isNetworkException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@KoinViewModel
class SelectCityViewModel(
    private val interactor: SelectCityInteractor,
    private val selectCityComponent: SelectCityComponent,
) : ViewModel(),
    ISideEffectSource<SelectCityViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()
    private val messageQueue = MessageQueue(viewModelScope)

    val isSearchLoadingVisible =
        operationTracker.isOperationOngoing(Operation.CITY_LOAD, Operation.ONBOARDING_FINISH)
            .debounce {
                if (it) Duration.ZERO else LOADER_STATE_DEBOUNCE_DURATION
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()
    private val _cities =
        MutableStateFlow<ImmutableList<SelectCityComponent.CityListItem>>(persistentListOf())
    val cities: StateFlow<ImmutableList<SelectCityComponent.CityListItem>> = _cities
    private val _isRegionVisible = MutableStateFlow(true)
    val isRegionVisible = _isRegionVisible.asStateFlow()
    private val _errorType = MutableStateFlow<SelectCityComponent.ErrorType?>(null)
    val errorType = _errorType.asStateFlow()
    val isSnackbarVisible = messageQueue.isMessageVisible
    val snackbarText = messageQueue.message

    init {
        viewModelScope.launch {
            query
                .map { it.trim() }
                // This artificial delay is a workaround for ktor CIO incorrectly throwing SocketException
                // when request is cancelled very early in it's lifecycle
                .debounce(CITY_FETCH_DEBOUNCE_DURATION)
                .collectLatest { fetchCities(it) }
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onCityClick(city: City) {
        viewModelScope.launch {
            operationTracker.track(Operation.ONBOARDING_FINISH) {
                interactor.finishOnboarding(city)
                    .onSuccess { sideEffect(SideEffect.ShowHome) }
                    .onFailure { throwable ->
                        val message = when {
                            throwable is CancellationException -> return@onFailure
                            throwable.isNetworkException() -> Text.Resource(R.string.network_error)
                            else -> Text.Resource(R.string.cant_save_selected_city)
                        }
                        messageQueue.showMessage(message)
                    }
            }
        }
    }

    fun onErrorButtonClick(
        @Suppress("UNUSED_PARAMETER")
        type: SelectCityComponent.ErrorType,
    ) {
        viewModelScope.launch {
            fetchCities(query.value)
        }
    }

    fun onCloseClick() {
        viewModelScope.launch {
            interactor.finishOnboarding(null)
                .onSuccess { sideEffect(SideEffect.ShowHome) }
        }
    }

    private suspend fun fetchCities(query: String?) {
        operationTracker.track(Operation.CITY_LOAD) {
            interactor.getCities(query)
                .onSuccess {
                    withContext(Dispatchers.IO) {
                        val isBaseList = query.isNullOrEmpty()
                        withContext(NonCancellable) {
                            _errorType.value =
                                if (it.isEmpty()) SelectCityComponent.ErrorType.NO_RESULTS else null
                            _isRegionVisible.value = !isBaseList
                            _cities.value = with(selectCityComponent) {
                                it.toCityListItems(priorityCitiesAtTop = isBaseList)
                            }
                        }
                    }
                }
                .onFailure { throwable ->
                    when {
                        throwable is CancellationException -> return@onFailure
                        throwable.isNetworkException() -> _errorType.value =
                            SelectCityComponent.ErrorType.NETWORK

                        else -> _errorType.value = SelectCityComponent.ErrorType.GENERIC
                    }
                }
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object ShowHome : SideEffect
    }

    enum class Operation : OperationKey { CITY_LOAD, ONBOARDING_FINISH }

    companion object {
        private val CITY_FETCH_DEBOUNCE_DURATION = 100.milliseconds
        private val LOADER_STATE_DEBOUNCE_DURATION = 250.milliseconds
    }

}
