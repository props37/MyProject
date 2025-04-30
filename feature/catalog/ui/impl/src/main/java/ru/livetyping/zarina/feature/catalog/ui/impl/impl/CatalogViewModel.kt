package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.component.GenderPickerComponent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.component.MenuComponent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuItem
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuState
import javax.inject.Inject

@HiltViewModel
internal class CatalogViewModel @Inject constructor(
    deps: CatalogDependencies,
) : ViewModel(), SideEffectSource<CatalogSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val genderPickerComponent = GenderPickerComponent(viewModelScope)
    private val menuComponent = MenuComponent(deps.getCatalogMenu)

    private val catalogInitialState = CatalogState(
        genderPickerState = genderPickerComponent.genderPickerState.value,
        menuState = MenuState.Loading,
    )

    private val catalogStateBuilder = CatalogState.Builder()
    val catalogState: StateFlow<CatalogState> = combine(
        genderPickerComponent.genderPickerState,
        menuComponent.menuResult,
        menuComponent.isMenuLoading,
        menuComponent.expandedMenuItemIds,
    ) { genderPickerState, menuResult, isMenuLoading, expandedMenuItemIds ->
        catalogStateBuilder.build(
            genderPickerState = genderPickerState,
            menuResult = menuResult,
            isMenuLoading = isMenuLoading,
            expandedMenuItemIds = expandedMenuItemIds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = catalogInitialState,
    )

    init {
        fetchMenu()
    }

    fun onCatalogEvent(event: CatalogEvent) {
        when (event) {
            CatalogEvent.BackClicked -> onBackClicked()
            is CatalogEvent.GenderSelected -> genderPickerComponent.onGenderSelected(event.tab)
            CatalogEvent.SearchClicked -> onSearchClicked()
            is CatalogEvent.MenuItemClicked -> onMenuItemClicked(event)
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CatalogScreenAction.BackClicked
            emitSideEffect(CatalogSideEffect.Navigate(action))
        }
    }

    private fun onSearchClicked() {
        navigationThrottler.throttle {
            val action = CatalogScreenAction.SearchClicked
            emitSideEffect(CatalogSideEffect.Navigate(action))
        }
    }

    private fun onMenuItemClicked(event: CatalogEvent.MenuItemClicked) {
        when (val item = event.item) {
            is MenuItem.Basic -> {
                if (item.item.isExpandable) {
                    menuComponent.toggleExpandableItem(item.item)
                } else {
                    // TODO: [Top] Implement
                }
            }

            MenuItem.SupportContactDetails -> Unit
            is MenuItem.Spacer -> Unit
        }
    }

    private fun fetchMenu() {
        viewModelScope.launch {
            menuComponent.fetchMenu(CachePolicy.LocalFirstThenRemote())
        }
    }
}
