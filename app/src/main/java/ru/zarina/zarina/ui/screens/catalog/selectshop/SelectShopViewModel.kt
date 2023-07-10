package ru.zarina.zarina.ui.screens.catalog.selectshop

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.ui.screens.catalog.filters.FiltersViewModel

@KoinViewModel
class SelectShopViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val filtersSavedStateHandle: SavedStateHandle,
    private val interactor: SelectShopInteractor,
) : ViewModel(),
    ISideEffectSource<SelectShopViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()

    private val newFiltration =
        filtersSavedStateHandle.getStateFlow<Filtration?>(FiltersViewModel.KEY_NEW_FILTRATION, null)

    private val cityResult = savedStateHandle.getStateFlow<Result<City>?>(KEY_CITY, null)

    val city = cityResult
        .map { it?.getOrNull() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val shops = cityResult.mapLatest { cityResult ->
        val city = cityResult?.getOrNull() ?: return@mapLatest persistentListOf()
        operationTracker.track(Operation.LOADING_SHOPS) {
            interactor.getShops(city)
                .getOrDefault(emptyList())
                .toPersistentList()
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())

    val isLoaderVisible =
        operationTracker.isOperationOngoing(Operation.LOADING_CITY, Operation.LOADING_SHOPS)
            .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val _selectedShop = MutableStateFlow(newFiltration.value?.pickupShop)
    val selectedShop = _selectedShop.asStateFlow()

    val isApplyButtonVisible = combine(newFiltration, _selectedShop) { filtration, shop ->
        filtration?.pickupShop != shop
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {
        viewModelScope.launch {
            operationTracker.track(Operation.LOADING_CITY) {
                savedStateHandle[KEY_CITY] = interactor.getUserCity()
            }
        }
    }

    fun onCityClick() {
        sideEffect(SideEffect.ShowSelectCity)
    }

    fun onShopClick(shop: Shop) {
        _selectedShop.update { if (it == shop) null else shop }
    }

    fun onApplyClick() {
        val selectedShop = _selectedShop.value
        filtersSavedStateHandle[FiltersViewModel.KEY_NEW_FILTRATION] =
            newFiltration.value?.copy(
                isPickupAvailable = selectedShop != null,
                pickupShop = selectedShop
            )
        sideEffect(SideEffect.GoBack)
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object ShowSelectCity : SideEffect
        object GoBack : SideEffect
    }

    enum class Operation : OperationKey { LOADING_CITY, LOADING_SHOPS }

    companion object {
        const val KEY_CITY = "city"
    }

}
