package ru.zarina.zarina.ui.screens.selectsize

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Size
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class SelectSizeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SelectSizeInteractor,
) : ViewModel(),
    ISideEffectSource<SelectSizeViewModel.SideEffect> by SideEffectQueue() {

    private val productId = savedStateHandle.getStateFlow(
        key = Destinations.SELECT_SIZE.ARGUMENT_PRODUCT_ID,
        initialValue = ""
    ).mapState(viewModelScope) { Product.Id(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    val sizes = productId
        .mapLatest { id ->
            // TODO show error
            // TODO show loading
            interactor.getProduct(id)
                .getOrNull()
                ?.offers
                ?.map { it.size }
                // TODO if empty, navigate back
                .orEmpty()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun onSizeClick(size: Size) {
        // TODO
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
