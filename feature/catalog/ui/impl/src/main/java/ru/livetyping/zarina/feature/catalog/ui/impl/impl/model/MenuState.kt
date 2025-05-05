package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenu
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState2
import ru.livetyping.zarina.core.uimodel.tab.GenderTab

@Stable
internal sealed class MenuState {
    @Immutable
    data class Success(val items: ImmutableList<MenuItem>) : MenuState()

    data object Loading : MenuState()

    // TODO: [Top] Implement when design is ready
    @Immutable
    data class Error(val state: ZarinaErrorScreenState2) : MenuState()

    class Builder {
        fun build(
            gender: GenderTab,
            menuResult: Result<CatalogMenuByGender>?,
            isMenuLoading: Boolean,
            expandedMenuItemIds: Set<CatalogMenuItem.Id>,
            city: City,
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

                        val items = buildMenuItems(
                            menu = menu,
                            expandedMenuItemIds = expandedMenuItemIds,
                            city = city,
                        )
                        Success(items.toImmutableList())
                    },
                    onFailure = {
                        val errorState = ZarinaErrorScreenState2.from(it)
                        Error(errorState)
                    },
                )
            }
        }

        private fun buildMenuItems(
            menu: CatalogMenu,
            expandedMenuItemIds: Set<CatalogMenuItem.Id>,
            city: City,
        ): List<MenuItem> {
            return buildList {
                menu.top?.let {
                    addCatalogMenuItems(it, expandedMenuItemIds, addBrackets = false)
                }

                addSpacerIfAbsent(MenuItem.Spacer.Size.MEDIUM)
                menu.middle?.let {
                    addCatalogMenuItems(it, expandedMenuItemIds, addBrackets = false)
                }

                addSpacerIfAbsent(MenuItem.Spacer.Size.MEDIUM)
                menu.bottom?.let {
                    addCatalogMenuItems(it, expandedMenuItemIds, addBrackets = true)
                }

                addSpacerIfAbsent(MenuItem.Spacer.Size.MEDIUM)
                add(MenuItem.City(city))

                addSpacerIfAbsent(MenuItem.Spacer.Size.SMALL)
                add(MenuItem.SupportContactDetails)
            }
        }

        private fun MutableList<MenuItem>.addCatalogMenuItems(
            items: List<CatalogMenuItem>,
            expandedItemIds: Set<CatalogMenuItem.Id>,
            addBrackets: Boolean,
        ) {
            items.forEach { parent ->
                addCatalogMenuItemWithChildren(
                    item = parent,
                    expandedItemIds = expandedItemIds,
                    currentNestingLevel = MenuItem.INITIAL_NESTING_LEVEL,
                    addBrackets = addBrackets,
                )
            }
        }

        private fun MutableList<MenuItem>.addCatalogMenuItemWithChildren(
            item: CatalogMenuItem,
            expandedItemIds: Set<CatalogMenuItem.Id>,
            currentNestingLevel: Int,
            addBrackets: Boolean,
        ) {
            val isExpanded = item.isExpandable && item.id in expandedItemIds
            val menuItem = MenuItem.Basic(
                item = item,
                isExpanded = isExpanded,
                addBrackets = addBrackets,
                nestingLevel = currentNestingLevel,
                isHighlighted = isExpanded || currentNestingLevel > MenuItem.INITIAL_NESTING_LEVEL,
            )

            if (isExpanded) {
                if (currentNestingLevel == MenuItem.INITIAL_NESTING_LEVEL) {
                    addSpacerIfAbsent(MenuItem.Spacer.Size.MEDIUM)
                }

                add(menuItem)

                item.children?.forEach { child ->
                    addCatalogMenuItemWithChildren(
                        item = child,
                        expandedItemIds = expandedItemIds,
                        currentNestingLevel = currentNestingLevel + 1,
                        addBrackets = addBrackets,
                    )
                }

                if (currentNestingLevel == MenuItem.INITIAL_NESTING_LEVEL) {
                    addSpacerIfAbsent(MenuItem.Spacer.Size.MEDIUM)
                }
            } else {
                add(menuItem)
            }
        }

        private fun MutableList<MenuItem>.addSpacerIfAbsent(size: MenuItem.Spacer.Size) {
            val prevItem = this.lastOrNull()
            if (prevItem != null && prevItem !is MenuItem.Spacer) {
                val id = "Spacer after ${prevItem.id}"
                add(MenuItem.Spacer(id, size))
            }
        }
    }
}
