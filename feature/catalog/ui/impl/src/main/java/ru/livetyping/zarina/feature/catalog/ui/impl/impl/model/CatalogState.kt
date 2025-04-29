package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState

@Immutable
internal data class CatalogState(
    val genderPickerState: TabRowState<GenderTab>,
    val menuState: MenuState,
) {
    class Builder {
        fun build(
            genderPickerState: TabRowState<GenderTab>,
            menuResult: Result<CatalogMenuByGender>?,
            isMenuLoading: Boolean,
            expandedMenuItemIds: Set<CatalogMenuItem.Id>,
        ): CatalogState {
            val menuState = buildMenuState(
                gender = genderPickerState.currentTab,
                menuResult = menuResult,
                isMenuLoading = isMenuLoading,
                expandedMenuItemIds = expandedMenuItemIds,
            )
            return CatalogState(
                genderPickerState = genderPickerState,
                menuState = menuState,
            )
        }

        private fun buildMenuState(
            gender: GenderTab,
            menuResult: Result<CatalogMenuByGender>?,
            isMenuLoading: Boolean,
            expandedMenuItemIds: Set<CatalogMenuItem.Id>,
        ): MenuState {
            return if (isMenuLoading || menuResult == null) {
                MenuState.Loading
            } else {
                menuResult.fold(
                    onSuccess = { menuByGender ->
                        val menu = when (gender) {
                            GenderTab.WOMEN -> menuByGender.women
                            GenderTab.MEN -> menuByGender.men
                        }

                        val topItems = menu.top?.let {
                            buildMenuItems(it, expandedMenuItemIds, addBrackets = false)
                        }?.toImmutableList()
                        val middleItems = menu.middle?.let {
                            buildMenuItems(it, expandedMenuItemIds, addBrackets = false)
                        }?.toImmutableList()
                        val bottomItems = menu.bottom?.let {
                            buildMenuItems(it, expandedMenuItemIds, addBrackets = true)
                        }?.toImmutableList()

                        MenuState.Success(topItems, middleItems, bottomItems)
                    },
                    onFailure = { MenuState.Error },
                )
            }
        }

        private fun buildMenuItems(
            items: List<CatalogMenuItem>,
            expandedItemIds: Set<CatalogMenuItem.Id>,
            addBrackets: Boolean,
        ): List<MenuItem> {
            return buildList {
                items.forEach { parent ->
                    val isParentExpanded = parent.isExpandable && parent.id in expandedItemIds
                    val parentItem = MenuItem.Generic(
                        item = parent,
                        isExpanded = isParentExpanded,
                        addBrackets = addBrackets,
                        addStartPadding = false,
                    )
                    add(parentItem)

                    if (isParentExpanded) {
                        parent.children?.forEach { child ->
                            val childItem = MenuItem.Generic(
                                item = child,
                                isExpanded = false,
                                addBrackets = addBrackets,
                                addStartPadding = true,
                            )
                            add(childItem)
                        }
                    }
                }
            }
        }
    }
}
