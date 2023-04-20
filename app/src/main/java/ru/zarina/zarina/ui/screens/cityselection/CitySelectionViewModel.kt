package ru.zarina.zarina.ui.screens.cityselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.MessageQueue
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.utils.isNetworkException
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@HiltViewModel
class CitySelectionViewModel @Inject constructor(
    private val interactor: CitySelectionInteractor,
) : ViewModel(),
    ISideEffectSource<CitySelectionViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()
    private val messageQueue = MessageQueue(viewModelScope)

    val isSearchLoadingVisible =
        operationTracker.isOperationOngoing(Operation.CITY_LOAD, Operation.ONBOARDING_FINISH)
            .debounce {
                if (it) LOADER_STATE_DEBOUNCE_DURATION else Duration.ZERO
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    private val _cities = MutableStateFlow<List<CityListItem>>(emptyList())
    val cities: StateFlow<List<CityListItem>> = _cities
    private val _isRegionVisible = MutableStateFlow(true)
    val isRegionVisible = _isRegionVisible.asStateFlow()
    private val _errorState = MutableStateFlow<ErrorState?>(null)
    val error = _errorState.asStateFlow()
    val isSnackbarVisible = messageQueue.isMessageVisible
    val snackbarText = messageQueue.message

    init {
        viewModelScope.launch {
            _query
                .map { it.trim() }
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

    fun onRefreshClick() {
        viewModelScope.launch {
            fetchCities(_query.value)
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
                            _errorState.value = if (it.isEmpty()) ErrorState.NO_RESULTS else null
                            _isRegionVisible.value = !isBaseList
                            _cities.value = it.toCityListItems(priorityCitiesAtTop = isBaseList)
                        }
                    }
                }
                .onFailure { throwable ->
                    when {
                        throwable is CancellationException -> return@onFailure
                        throwable.isNetworkException() -> _errorState.value = ErrorState.NETWORK
                        else -> _errorState.value = ErrorState.GENERIC
                    }
                }
        }
    }

    private fun List<City>.toCityListItems(
        priorityCitiesAtTop: Boolean,
    ): List<CityListItem> = buildList {
        var previousStartingLetter: Char? = null
        val (priorityCities, regularCities) = if (priorityCitiesAtTop)
            this@toCityListItems.partition { it.priority != null }
        else
            emptyList<City>() to this@toCityListItems

        addAll(priorityCities.sortedBy { it.priority }.map { CityListItem.Item(it) })

        regularCities.sortedBy { it.name }.forEach { city ->
            if (city.name.isEmpty()) return@forEach
            if (city.name.first() != previousStartingLetter) {
                previousStartingLetter = city.name.first()
                add(CityListItem.Header(previousStartingLetter.toString()))
            }
            add(CityListItem.Item(city))
        }
    }

    sealed class CityListItem(val key: String, val contentType: String) {
        data class Header(val letter: String) : CityListItem(letter, "header")
        data class Item(val city: City) : CityListItem(city.id.id, "item")
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object ShowHome : SideEffect
    }

    enum class Operation : OperationKey { CITY_LOAD, ONBOARDING_FINISH }

    enum class ErrorState { NO_RESULTS, NETWORK, GENERIC }

    companion object {
        private val LOADER_STATE_DEBOUNCE_DURATION = 250.milliseconds
    }

}
