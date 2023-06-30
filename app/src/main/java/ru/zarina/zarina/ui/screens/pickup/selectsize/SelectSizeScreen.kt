package ru.zarina.zarina.ui.screens.pickup.selectsize

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Barcode
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Size
import ru.zarina.zarina.ui.common.components.bottomsheet.Header
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.screens.pickup.PickupViewModel
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectSizeScreenContent(
    offers: List<Offer>,
    onOfferClick: (Offer) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(UiKitTheme.colors.screenBackground)
    ) {
        Header(
            text = stringResource(id = R.string.select_size_appeal),
            modifier = Modifier
                .padding(horizontal = 16.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            offers.forEach { offer ->
                SizeItem(
                    size = offer.size,
                    isAvailable = offer.isAvailable,
                    onClick = { onOfferClick(offer) },
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun SizeItem(
    size: Size,
    isAvailable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor by animateColorAsState(
        if (isAvailable) UiKitTheme.colors.primaryContentColor else UiKitTheme.colors.disabled,
        label = "size text color"
    )
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Text(
                text = size.name,
                style = UiKitTheme.typography.circle1718,
                color = contentColor,
                textAlign = TextAlign.Start,
                modifier = modifier
            )
            Spacer(modifier = Modifier.weight(1f))
            if (!isAvailable)
                Subscribe()
        }
    }
}

@Composable
private fun Subscribe(
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.subscribe),
            style = UiKitTheme.typography.circle1718,
            modifier = Modifier.padding(end = 8.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.ic_chevron_right_24),
            contentDescription = null,
        )
    }
}

@Composable
fun SelectSizeScreen(
    parentEntry: NavBackStackEntry,
    showSubscribe: (Barcode) -> Unit,
    goBack: () -> Unit,
) {
    val parentViewModel = koinViewModel<PickupViewModel>(viewModelStoreOwner = parentEntry)
    val viewModel = koinViewModel<SelectSizeViewModel>()

    val sizes by parentViewModel.offers.collectAsStateWithLifecycle()

    SelectSizeScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
        showSubscribe = showSubscribe,
    )

    SelectSizeScreenContent(
        offers = sizes,
        onOfferClick = {
            parentViewModel.onOfferClick(it)
            viewModel.onOfferClick(it)
        },
    )
}

@Composable
fun SelectSizeScreenBehavior(
    sideEffects: Flow<SelectSizeViewModel.SideEffect>,
    goBack: () -> Unit,
    showSubscribe: (Barcode) -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SelectSizeViewModel.SideEffect.GoBack -> goBack()
                is SelectSizeViewModel.SideEffect.ShowSubscribe -> showSubscribe(effect.offerBarcode)
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
    ZarinaTheme {
        SelectSizeScreenContent(
            offers = product.offers,
            onOfferClick = {},
        )
    }
}
