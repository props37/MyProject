package ru.zarina.zarina.ui.screens.selectsize

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Size
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectSizeScreenContent(
    sizes: List<Size>,
    onSizeClick: (Size) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(UiKitTheme.colors.screenBackground)
    ) {
        Header(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 8.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            sizes.forEach { size ->
                SizeItem(
                    size = size,
                    onClick = { onSizeClick(size) },
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.select_size_appeal),
        style = UiKitTheme.typography.sizePickerHeader,
        textAlign = TextAlign.Start,
        modifier = modifier
    )
}

@Composable
private fun SizeItem(
    size: Size,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Text(
            text = size.name,
            style = UiKitTheme.typography.sizePickerItem,
            textAlign = TextAlign.Start,
            modifier = modifier
        )
    }
}

@Composable
fun SelectSizeScreen() {
    val viewModel = hiltViewModel<SelectSizeViewModel>()

    val sizes by viewModel.sizes.collectAsStateWithLifecycle()

    SelectSizeScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SelectSizeScreenContent(
        sizes = sizes,
        onSizeClick = viewModel::onSizeClick,
    )
}

@Composable
fun SelectSizeScreenBehavior(
    sideEffects: Flow<SelectSizeViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun SelectSizeScreenContentPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    val sizes = product.offers.map { it.size }.toList()
    ZarinaTheme {
        SelectSizeScreenContent(
            sizes = sizes,
            onSizeClick = {},
        )
    }
}
