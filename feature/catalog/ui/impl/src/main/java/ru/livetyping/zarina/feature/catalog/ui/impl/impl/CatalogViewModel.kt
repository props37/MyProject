package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.ClickAction
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicomponent.gender.GenderPickerComponent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.component.MenuComponent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuItem
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuState
import javax.inject.Inject

@HiltViewModel
internal class CatalogViewModel @Inject constructor(
    private val deps: CatalogDependencies,
) : ViewModel(), SideEffectSource<CatalogSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val genderPickerComponent = GenderPickerComponent(viewModelScope)
    private val menuComponent = MenuComponent(deps.getCatalogMenu)

    private val catalogInitialState = CatalogState(
        genderPickerState = genderPickerComponent.genderPickerState.value,
        menuState = MenuState.Loading,
    )

    private val userCityFlow = deps.getUserCityFlow(
        params = GetUserCityFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())
    ).map { it.getOrNull() ?: City.getDefault() }

    private val catalogStateBuilder = CatalogState.Builder()
    val catalogState: StateFlow<CatalogState> = combine(
        genderPickerComponent.genderPickerState,
        menuComponent.menuResult,
        menuComponent.isMenuLoading,
        menuComponent.expandedMenuItemIds,
        userCityFlow,
    ) { genderPickerState, menuResult, isMenuLoading, expandedMenuItemIds, userCity ->
        catalogStateBuilder.build(
            genderPickerState = genderPickerState,
            menuResult = menuResult,
            isMenuLoading = isMenuLoading,
            expandedMenuItemIds = expandedMenuItemIds,
            city = userCity,
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
            CatalogEvent.LeaveFeedbackClicked -> onLeaveFeedbackClicked()
            CatalogEvent.ChangeCityClicked -> onChangeCityClicked()
            CatalogEvent.RefreshClicked -> fetchMenu()
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> deps.appMetrica.reportScreenOpened(Screen.Catalog)
            LifecycleEvent.ON_START -> Unit
            LifecycleEvent.ON_RESUME -> Unit
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
            is MenuItem.Basic -> onMenuItemBasicClicked(item)
            else -> Unit
        }
    }

    private fun onLeaveFeedbackClicked() {
        // TODO: [Top] Implement
    }

    private fun onChangeCityClicked() {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val city = userCityFlow.firstOrNull() ?: City.getDefault()
                val action = CatalogScreenAction.CityClicked(city)
                emitSideEffect(CatalogSideEffect.Navigate(action))
            }
        }
    }

    private fun onMenuItemBasicClicked(item: MenuItem.Basic) {
        if (item.item.isExpandable) {
            menuComponent.toggleExpandableItem(item.item)
        } else {
            navigationThrottler.throttle {
                when (val clickAction = item.item.clickAction) {
                    is ClickAction.OpenProductList -> {
                        val action = CatalogScreenAction.CategoryClicked(clickAction.categoryId)
                        emitSideEffect(CatalogSideEffect.Navigate(action))
                    }

                    is ClickAction.OpenUrl -> {
                        val action = CatalogScreenAction.UrlClicked(clickAction.url)
                        emitSideEffect(CatalogSideEffect.Navigate(action))
                    }
                }
            }
        }
    }

    private fun fetchMenu() {
        viewModelScope.launch {
            menuComponent.fetchMenu(CachePolicy.LocalFirstThenRemote())
        }
    }
}
