package ru.zarina.zarina.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun HomeScreenContent(
    onProductClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.screenBackground)
    ) {
        ZarinaTextButton(
            text = "Тестовый продукт",
            onClick = onProductClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
fun HomeScreen(
    showProduct: (id: Product.Id) -> Unit,
) {
    val viewModel = koinViewModel<HomeViewModel>()

    HomeScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProduct = showProduct,
    )

    HomeScreenContent(
        onProductClick = viewModel::onProductClick,
    )
}

@Composable
fun HomeScreenBehavior(
    sideEffects: Flow<HomeViewModel.SideEffect>,
    showProduct: (Product.Id) -> Unit,
) {
    NavigationBarState(isVisible = true, isAnimated = true)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is HomeViewModel.SideEffect.ShowProduct -> showProduct(effect.id)
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun HomeScreenContentPreview() {
    ZarinaTheme {
        HomeScreenContent(
            onProductClick = {},
        )
    }
}
