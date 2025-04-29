package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuItem
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuState

@Suppress("NAME_SHADOWING")
@Composable
internal fun Menu(
    state: MenuState,
    onCatalogEvent: (CatalogEvent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is MenuState.Success -> MenuContentKey.Success
                MenuState.Error -> it
                MenuState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is MenuState.Success -> {
                MenuSuccess(
                    state = state,
                    onCatalogEvent = onCatalogEvent,
                    contentPadding = contentPadding,
                )
            }

            MenuState.Loading -> Unit // TODO: [Top] Implement
            MenuState.Error -> Unit // TODO: [Top] Implement
        }
    }
}

@Composable
private fun MenuSuccess(
    state: MenuState.Success,
    onCatalogEvent: (CatalogEvent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(
            items = state.items,
            key = { item -> item.id },
        ) { item ->
            when (item) {
                is MenuItem.Basic -> {
                    MenuItemBasic(
                        item = item,
                        onClick = { onCatalogEvent(CatalogEvent.MenuItemClicked(it)) },
                        modifier = Modifier.animateZarinaItem(this),
                    )
                }

                is MenuItem.Spacer -> {
                    Spacer(
                        modifier = Modifier
                            .height(SpacerHeight)
                            .animateZarinaItem(this),
                    )
                }
            }
        }
    }
}

private enum class MenuContentKey { Success }

private val SpacerHeight: Dp get() = 20.dp
