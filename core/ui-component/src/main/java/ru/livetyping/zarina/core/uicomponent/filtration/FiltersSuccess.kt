package ru.livetyping.zarina.core.uicomponent.filtration

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicomponent.R
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider

@Composable
internal fun FiltersSuccess(
    state: FiltrationState.Success,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            // TODO: [Top] Implement
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
