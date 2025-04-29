package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenu
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

                        val items = buildMenuItems(menu, expandedMenuItemIds).toImmutableList()
                        MenuState.Success(items)
                    },
                    onFailure = { MenuState.Error },
                )
            }
        }

        private fun buildMenuItems(
            menu: CatalogMenu,
            expandedMenuItemIds: Set<CatalogMenuItem.Id>,
        ): List<MenuItem> {
            return buildList {
                menu.top?.let {
                    addCatalogMenuItems(it, expandedMenuItemIds, addBrackets = false)
                }

                addSpacerIfAbsent()
                menu.middle?.let {
                    addCatalogMenuItems(it, expandedMenuItemIds, addBrackets = false)
                }

                addSpacerIfAbsent()
                menu.bottom?.let {
                    addCatalogMenuItems(it, expandedMenuItemIds, addBrackets = true)
                }
            }
        }

        private fun MutableList<MenuItem>.addCatalogMenuItems(
            items: List<CatalogMenuItem>,
            expandedItemIds: Set<CatalogMenuItem.Id>,
            addBrackets: Boolean,
        ) {
            items.forEach { parent ->
                val isParentExpanded = parent.isExpandable && parent.id in expandedItemIds
                val parentItem = MenuItem.Basic(
                    item = parent,
                    isExpanded = isParentExpanded,
                    addBrackets = addBrackets,
                    addStartPadding = false,
                    isHighlighted = isParentExpanded,
                )

                if (isParentExpanded) {
                    addSpacerIfAbsent()
                    add(parentItem)

                    parent.children?.forEach { child ->
                        val childItem = MenuItem.Basic(
                            item = child,
                            isExpanded = false,
                            addBrackets = addBrackets,
                            addStartPadding = true,
                            isHighlighted = true,
                        )
                        add(childItem)
                    }

                    addSpacerIfAbsent()
                } else {
                    add(parentItem)
                }
            }
        }

        private fun MutableList<MenuItem>.addSpacerIfAbsent() {
            val prevItem = this.lastOrNull()
            if (prevItem != null && prevItem !is MenuItem.Spacer) {
                val id = "Spacer after ${prevItem.id}"
                add(MenuItem.Spacer(id))
            }
        }
    }
}
