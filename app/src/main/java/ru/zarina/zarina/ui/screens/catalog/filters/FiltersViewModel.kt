package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.PriceRange
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.screens.catalog.products.ProductsViewModel
import javax.inject.Inject

@KoinViewModel
class FiltersViewModel @Inject constructor(
    productsSavedStateHandle: SavedStateHandle,
    private val interactor: FiltersInteractor,
) : ViewModel(),
    ISideEffectSource<FiltersViewModel.SideEffect> by SideEffectQueue() {

    private val appliedFiltration = productsSavedStateHandle
        .getStateFlow<Filtration?>(ProductsViewModel.KEY_FILTRATION, null)
    private val _newFiltration = MutableStateFlow(appliedFiltration.value)
    val newFiltration = _newFiltration.asStateFlow()

    val filterButtonMode = combine(appliedFiltration, _newFiltration) { applied, new ->
        if (applied != new) FilterButtonMode.APPLY else FilterButtonMode.CLOSE
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), FilterButtonMode.CLOSE)

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onPriceChange(min: Int, max: Int) {
        _newFiltration.update { it?.copy(price = PriceRange(min, max)) }
    }

    fun onFilterButtonClick() {
        // TODO send result
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

    enum class FilterButtonMode { APPLY, CLOSE }

}
