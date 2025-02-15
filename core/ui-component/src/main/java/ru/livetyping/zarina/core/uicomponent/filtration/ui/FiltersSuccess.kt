package ru.livetyping.zarina.core.uicomponent.filtration.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductPriceFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductToggleFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.uicomponent.R
import ru.livetyping.zarina.core.uicomponent.filtration.model.FiltrationState
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem

@Composable
internal fun FiltersSuccess(
    state: FiltrationState.Success,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val listState = rememberLazyListState()
        val isDragged = listState.interactionSource.collectIsDraggedAsState()
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(isDragged, keyboardController) {
            snapshotFlow { isDragged.value }
                .collect { keyboardController?.hide() }
        }

        val filters = state.filters
        val filterCount = remember(filters) { filters.iterator().asSequence().count() }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            filters.forEachIndexed { index, filter ->
                when {
                    filter.type == ProductFilter.Type.PICKUP_STORES
                            && !state.isPickupStoreFilterVisible -> Unit

                    else -> {
                        item(
                            key = filter.type,
                            contentType = {
                                when (filter) {
                                    is ProductPriceFilter -> FilterContentType.PriceFilter
                                    is ProductListFilter<*> -> FilterContentType.ListFilter
                                    is ProductToggleFilter -> FilterContentType.ToggleFilter
                                    else -> null
                                }
                            },
                        ) {
                            Column(modifier = Modifier.animateZarinaItem(this)) {
                                Filter(filter)

                                if (filter !is ProductPriceFilter && index < filterCount - 1) {
                                    ZarinaDivider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        ZarinaDivider(modifier = Modifier.fillMaxWidth())

        ShowProductsButton(
            productCount = state.availableProductCount,
            onClick = { TODO() }, // TODO: [Top] Implement
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
private fun Filter(
    filter: ProductFilter<*>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (filter) {
            is ProductPriceFilter -> {
                val horizontalPadding = 16.dp
                val systemGestureHorizontalPadding = WindowInsets.safeGestures
                    .asPaddingValues()
                    .calculateLeftPadding(LocalLayoutDirection.current)
                val sliderHorizontalPadding =
                    (systemGestureHorizontalPadding - horizontalPadding)
                        .coerceAtLeast(0.dp)

                PriceFilter(
                    filter = filter,
                    onFilterChanged = {}, // TODO: [Top] Implement
                    sliderAdditionalHorizontalPadding = sliderHorizontalPadding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = horizontalPadding,
                            top = 16.dp,
                            end = horizontalPadding,
                            bottom = 8.dp,
                        ),
                )
            }

            is ProductListFilter<*> -> {
                if (filter.isSingleSelection) {
                    SingleSelectionFilterItem(
                        type = filter.type,
                        selected = filter.selectedItems.firstOrNull(),
                        onClick = { TODO() }, // TODO: [Top] Implement
                    )
                } else {
                    MultiSelectionListFilter(
                        type = filter.type,
                        selectedCount = filter.selectedItems.size,
                        onClick = { TODO() }, // TODO: [Top] Implement
                    )
                }
            }

            is ProductToggleFilter -> {
                ToggleFilter(
                    type = filter.type,
                    isChecked = filter.isEnabled,
                    onCheckedChanged = {
                        TODO() // TODO: [Top] Implement
                    },
                )
            }
        }
    }
}

@Composable
private fun ShowProductsButton(
    productCount: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaButton(
        onClick = onClick,
        isEnabled = productCount == null || productCount > 0,
        modifier = modifier,
    ) {
        AnimatedContent(
            targetState = productCount,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec.using(sizeTransform = null)
            },
            contentAlignment = Alignment.Center,
            label = "ShowProductsButton",
        ) { productCount ->
            val text = when {
                productCount == null -> stringResource(R.string.ui_component_show_products)
                productCount > 0 -> {
                    pluralStringResource(
                        id = R.plurals.ui_component_show_products,
                        count = productCount,
                        productCount.toString(),
                    )
                }

                else -> stringResource(R.string.ui_component_products_not_found)
            }

            Text(text = text.uppercase())
        }
    }
}

private enum class FilterContentType {
    PriceFilter,
    ListFilter,
    ToggleFilter,
}
