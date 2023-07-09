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
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class SelectShopViewModel(
    private val filtersSavedStateHandle: SavedStateHandle,
    private val interactor: SelectShopInteractor,
) : ViewModel(),
    ISideEffectSource<SelectShopViewModel.SideEffect> by SideEffectQueue() {

    // TODO load user current city
    val city = MutableStateFlow(City.DEFAULT)

    @OptIn(ExperimentalCoroutinesApi::class)
    val shops = city.mapLatest { city ->
        // TODO loader
        interactor.getShops(city)
            .getOrDefault(emptyList())
            .toPersistentList()
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())

    private val _selectedShop = MutableStateFlow<Shop?>(null)
    val selectedShop = _selectedShop.asStateFlow()

    fun onShopClick(shop: Shop) {
        _selectedShop.update { if (it == shop) null else shop }
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
