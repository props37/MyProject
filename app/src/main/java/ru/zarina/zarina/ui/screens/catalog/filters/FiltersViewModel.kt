package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.PriceRange
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
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
        savedStateHandle.getStateFlow(KEY_NEW_FILTRATION, appliedFiltration.value)

    val isClearButtonVisible = combine(baseFiltration, newFiltration) { base, new ->
        new != base
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val filterButtonMode = combine(appliedFiltration, newFiltration) { applied, new ->
        if (applied != new) FilterButtonMode.APPLY else FilterButtonMode.CLOSE
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), FilterButtonMode.CLOSE)

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onClearClick() {
        savedStateHandle[KEY_NEW_FILTRATION] = baseFiltration.value
    }

    fun onPriceChange(min: Int, max: Int) {
        savedStateHandle[KEY_NEW_FILTRATION] =
            baseFiltration.value?.copy(price = PriceRange(min, max))
    }

    fun onFilterButtonClick() {
        productsSavedStateHandle[ProductsViewModel.KEY_REQUESTED_FILTRATION] = newFiltration.value
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

    enum class FilterButtonMode { APPLY, CLOSE }

    companion object {
        private const val KEY_NEW_FILTRATION = "new_filtration"
    }

}
