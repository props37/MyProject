package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Suppress("NAME_SHADOWING")
@Composable
internal fun AddressSearch(
    state: AddressSearchState,
    onAddressItemClicked: (AddressSearchItem) -> Unit,
    onErrorRefreshClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is AddressSearchState.Success -> ContentKey.Success
                AddressSearchState.Empty -> it
                is AddressSearchState.Error -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is AddressSearchState.Success -> {
                AddressSearchSuccess(
                    state = state,
                    onAddressItemClicked = onAddressItemClicked,
                )
            }

            is AddressSearchState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = onErrorRefreshClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(windowInsetsProvider())
                        .padding(16.dp),
                )
            }

            AddressSearchState.Empty -> Unit
        }
    }
}

@Composable
private fun AddressSearchSuccess(
    state: AddressSearchState.Success,
    onAddressItemClicked: (AddressSearchItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentPadding = PaddingValues(
        bottom = ZarinaScrollableDefaults.ScrollableBottomPadding,
    )

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        itemsIndexed(
            items = state.items,
            key = { _, item -> item.address.id.value },
        ) { index, item ->
            Column(modifier = Modifier.animateZarinaItem(this)) {
                ZarinaItem(
                    onClick = { onAddressItemClicked(item) },
                ) {
                    Text(
                        text = item.address.name,
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )
                }
            }

            if (index < state.items.lastIndex) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

private enum class ContentKey { Success }
