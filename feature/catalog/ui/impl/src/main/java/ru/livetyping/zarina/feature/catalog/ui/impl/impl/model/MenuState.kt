package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenu
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem
import ru.livetyping.zarina.core.uimodel.tab.GenderTab

@Stable
internal sealed class MenuState {
    @Immutable
    data class Success(val items: ImmutableList<MenuItem>) : MenuState()

    data object Loading : MenuState()

    // TODO: [Top] Implement when design is ready
    data object Error : MenuState()

    class Builder {
        fun build(
            gender: GenderTab,
            menuResult: Result<CatalogMenuByGender>?,
            isMenuLoading: Boolean,
            expandedMenuItemIds: Set<CatalogMenuItem.Id>,
        ): MenuState {
            return if (isMenuLoading || menuResult == null) {
                Loading
            } else {
                menuResult.fold(
                    onSuccess = { menuByGender ->
                        val menu = when (gender) {
                            GenderTab.WOMEN -> menuByGender.women
                            GenderTab.MEN -> menuByGender.men
                        }

                        val items = buildMenuItems(menu, expandedMenuItemIds).toImmutableList()
                        Success(items)
                    },
                    onFailure = { Error },
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
