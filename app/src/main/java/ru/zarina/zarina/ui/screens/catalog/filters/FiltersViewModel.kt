package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.PriceRange
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.components.FilterButtonMode
import ru.zarina.zarina.ui.screens.catalog.products.ProductsViewModel

@KoinViewModel
class FiltersViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val productsSavedStateHandle: SavedStateHandle,
    private val interactor: FiltersInteractor,
) : ViewModel(),
    ISideEffectSource<FiltersViewModel.SideEffect> by SideEffectQueue() {

    private val baseFiltration = productsSavedStateHandle
        .getStateFlow<Filtration?>(ProductsViewModel.KEY_BASE_FILTRATION, null)
    private val appliedFiltration = productsSavedStateHandle
        .getStateFlow<Filtration?>(ProductsViewModel.KEY_REQUESTED_FILTRATION, null)
    val newFiltration =
        savedStateHandle.getStateFlow<Filtration?>(KEY_NEW_FILTRATION, null)

    val isClearButtonVisible = combine(baseFiltration, newFiltration) { base, new ->
        new != base
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val filterButtonMode = combine(appliedFiltration, newFiltration) { applied, new ->
        if (applied != new) FilterButtonMode.APPLY else FilterButtonMode.CLOSE
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), FilterButtonMode.CLOSE)

    init {
        viewModelScope.launch {
            val appliedFiltration = appliedFiltration.first { it != null }
            if (newFiltration.value == null) savedStateHandle[KEY_NEW_FILTRATION] =
                appliedFiltration
        }
    }

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onClearClick() {
        savedStateHandle[KEY_NEW_FILTRATION] = baseFiltration.value
    }

    fun onPriceChange(min: Int, max: Int) {
        savedStateHandle[KEY_NEW_FILTRATION] =
            newFiltration.value?.copy(price = PriceRange(min, max))
    }

    fun onIsShippingAvailableChange(isAvailable: Boolean) {
        savedStateHandle[KEY_NEW_FILTRATION] =
            newFiltration.value?.copy(isShippingAvailable = isAvailable)
    }

    fun onIsPickupAvailableChange(isAvailable: Boolean) {
        savedStateHandle[KEY_NEW_FILTRATION] =
            newFiltration.value?.copy(isPickupAvailable = isAvailable)
    }

    fun onFilterClick(type: FilterType) {
        when (type) {
            FilterType.CATEGORY -> sideEffect(SideEffect.ShowTreeFilter(type))
            else -> sideEffect(SideEffect.ShowListFilter(type))
        }
    }

    fun onFilterButtonClick() {
        productsSavedStateHandle[ProductsViewModel.KEY_REQUESTED_FILTRATION] = newFiltration.value
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        class ShowListFilter(val type: FilterType) : SideEffect
        class ShowTreeFilter(val type: FilterType) : SideEffect
        object GoBack : SideEffect
    }

    companion object {
        const val KEY_NEW_FILTRATION = "new_filtration"
    }

}
