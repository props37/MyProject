package ru.zarina.zarina.ui.screens.product

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.components.MediaPager
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ProductScreenContent(
    product: Product?,
    cache: State<Cache?>,
) {
    MediaPager(
        media = product?.media.orEmpty(),
        cache = cache,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun ProductScreen() {
    val viewModel = hiltViewModel<ProductViewModel>()

    val cache = viewModel.cache.collectAsStateWithLifecycle()
    val product by viewModel.product.collectAsStateWithLifecycle()

    ProductScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    ProductScreenContent(
        product = product,
        cache = cache,
    )
}

@Composable
fun ProductScreenBehavior(
    sideEffects: Flow<ProductViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun ProductScreenContentPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        ProductScreenContent(
            product = product,
            cache = remember { mutableStateOf(null) },
        )
    }
}
