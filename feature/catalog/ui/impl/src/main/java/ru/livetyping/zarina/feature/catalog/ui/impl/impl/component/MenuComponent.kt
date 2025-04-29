package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem
import ru.livetyping.zarina.core.domain.usecase.catalog.GetCatalogMenuUseCase
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker

internal class MenuComponent(
    private val getCatalogMenuUseCase: GetCatalogMenuUseCase,
) {
    private val operationTracker = OperationTracker()

    private var menuJob: Job? = null

    private val _menuResult = MutableStateFlow<Result<CatalogMenuByGender>?>(null)
    val menuResult: StateFlow<Result<CatalogMenuByGender>?> = _menuResult.asStateFlow()

    val isMenuLoading: Flow<Boolean> = operationTracker.isOperationOngoing(MenuRequest)

    private val _expandedMenuItemIds = MutableStateFlow<Set<CatalogMenuItem.Id>>(emptySet())
    val expandedMenuItemIds: StateFlow<Set<CatalogMenuItem.Id>> = _expandedMenuItemIds.asStateFlow()

    suspend fun fetchMenu(cachePolicy: CachePolicy) {
        if (menuJob?.isActive == true) return

        coroutineScope {
            val params = GetCatalogMenuUseCase.Params(cachePolicy)
            menuJob = launch {
                operationTracker.track(MenuRequest) {
                    _menuResult.value = getCatalogMenuUseCase(params)
                }
            }
        }
    }

    fun toggleExpandableItem(item: CatalogMenuItem) {
        _expandedMenuItemIds.update { set ->
            val id = item.id
            if (id in set) set - id else set + id
        }
    }

    private data object MenuRequest : OperationKey
}
