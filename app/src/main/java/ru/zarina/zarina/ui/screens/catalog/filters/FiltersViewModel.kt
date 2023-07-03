package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onPriceChange(min: Int, max: Int) {
        _newFiltration.update { it?.copy(price = PriceRange(min, max)) }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
