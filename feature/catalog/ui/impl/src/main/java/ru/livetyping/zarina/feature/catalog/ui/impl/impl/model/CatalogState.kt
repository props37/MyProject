package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
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
        private val menuStateBuilder = MenuState.Builder()

        fun build(
            genderPickerState: TabRowState<GenderTab>,
            menuResult: Result<CatalogMenuByGender>?,
            isMenuLoading: Boolean,
            expandedMenuItemIds: Set<CatalogMenuItem.Id>,
        ): CatalogState {
            val menuState = menuStateBuilder.build(
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
    }
}
