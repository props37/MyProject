package ru.livetyping.zarina.core.uicomponent.filtration.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationState
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader

@Suppress("NAME_SHADOWING")
@Composable
internal fun ProductFilters(
    state: ProductFiltrationState,
    onEvent: (ProductFiltrationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is ProductFiltrationState.Success -> FiltersContentKey.Success
                is ProductFiltrationState.Error, ProductFiltrationState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is ProductFiltrationState.Success -> {
                ProductFiltersSuccess(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            ProductFiltrationState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                }
            }

            is ProductFiltrationState.Error -> {
                ZarinaErrorScreen(
                    state = state.errorState,
                    onButtonClicked = { onEvent(ProductFiltrationEvent.ErrorRefreshClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}

private enum class FiltersContentKey { Success }
