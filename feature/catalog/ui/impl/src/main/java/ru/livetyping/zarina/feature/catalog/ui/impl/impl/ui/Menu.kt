package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogEvent
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

            MenuState.Loading -> {
                MenuLoading(contentPadding = contentPadding)
            }

            MenuState.Error -> Unit // TODO: [Top] Implement
        }
    }
}

private enum class MenuContentKey { Success }
