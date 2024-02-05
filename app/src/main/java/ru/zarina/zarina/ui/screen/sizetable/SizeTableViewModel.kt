package ru.zarina.zarina.ui.screen.sizetable

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizetable.SizeTableViewModel.SideEffect
import ru.zarina.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class SizeTableViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SizeTableInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val product: StateFlow<Product> = savedStateHandle
        .getStateFlow<ProductParcelable?>(
            key = UnscopedDestinations.SizeTable.ARG_KEY_PRODUCT,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "product is null" }
            parcelable.toProduct()
        }

    sealed interface SideEffect : SideEffectSource.SideEffect
}
