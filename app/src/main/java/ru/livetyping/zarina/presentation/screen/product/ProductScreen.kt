package ru.livetyping.zarina.presentation.screen.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaBottomSheetDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.product.ProductScreenComponents.ProductDetails
import ru.livetyping.zarina.presentation.screen.product.ProductScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.product.ProductScreenComponents.TopBarMode
import ru.livetyping.zarina.presentation.screen.product.ProductScreenComponents.topBarModeAsState
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel.ProductState
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel.SuggestedProductListState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarLayout

@Composable
fun ProductScreen(
    navigate: (ProductScreenAction) -> Unit,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val productState by viewModel.productState.collectAsStateWithLifecycle()
    val productTotalLookState by viewModel.productTotalLookState.collectAsStateWithLifecycle()
    val productSimilarState by viewModel.productSimilarState.collectAsStateWithLifecycle()

    ScreenContent(
        productState = productState,
        onProductColorClicked = viewModel::onProductColorClicked,
        onAddProductToCartClicked = viewModel::onAddProductToCartClicked,
        onAddProductToFavoritesClicked = viewModel::onAddProductToFavoritesClicked,
        onProductErrorRefreshClicked = viewModel::onProductErrorRefreshClicked,
        onProductClicked = viewModel::onProductClicked,
        productTotalLookState = productTotalLookState,
        onProductTotalLookErrorRefreshClicked = viewModel::onProductTotalLookErrorRefreshClicked,
        productSimilarState = productSimilarState,
        onProductSimilarErrorRefreshClicked = viewModel::onProductSimilarErrorRefreshClicked,
        onBackClicked = viewModel::onBackClicked,
        onShareClicked = viewModel::onShareClicked,
        onUrlClicked = viewModel::onUrlClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    productState: ProductState,
    onProductColorClicked: (ProductColor) -> Unit,
    onAddProductToCartClicked: (Product) -> Unit,
    onAddProductToFavoritesClicked: (Product) -> Unit,
    onProductErrorRefreshClicked: () -> Unit,
    onProductClicked: (Product) -> Unit,
    productTotalLookState: SuggestedProductListState,
    onProductTotalLookErrorRefreshClicked: () -> Unit,
    productSimilarState: SuggestedProductListState,
    onProductSimilarErrorRefreshClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProductScreenAction) -> Unit,
) {
    ProductScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    // TODO: [High] Extract
    val coroutineScope = rememberCoroutineScope()
    var isZarinaClubBottomSheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    if (isZarinaClubBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isZarinaClubBottomSheetVisible = false },
            shape = ZarinaBottomSheetDefaults.Shape,
            containerColor = UiKitTheme.colors.background.general.regular.default,
            contentColor = UiKitTheme.colors.text.general.regular.default,
            tonalElevation = 0.dp,
            scrimColor = ZarinaBottomSheetDefaults.ScrimColor,
            dragHandle = {},
        ) {
            // TODO: [High] Extract
            Row(
                modifier = Modifier
                    .heightIn(min = TopBarDefaults.MinHeight)
                    .padding(
                        start = 16.dp,
                        top = 4.dp,
                        end = 2.dp,
                        bottom = 4.dp,
                    ),
            ) {
                Text(
                    text = stringResource(R.string.for_zarina_club_members),
                    style = UiKitTheme.typography.primary.bold,
                    color = UiKitTheme.colors.text.general.regular.default,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                ZarinaCloseIconButton(
                    onClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            isZarinaClubBottomSheetVisible = false
                        }
                    },
                    iconSize = 20.dp,
                )
            }

            Text(
                text = stringResource(R.string.zarina_club_program_description_1),
                style = UiKitTheme.typography.secondary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.zarina_club_program_description_2),
                style = UiKitTheme.typography.secondary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp),
            )
        }
    }

    val lazyListState = rememberLazyListState()
    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior(
        canScroll = { lazyListState.canScrollForward },
        scrollBeforeContent = { false },
    )

    CollapsingTopBarLayout(
        topBar = {
            val mode = if (productState is ProductState.Success) {
                topBarModeAsState(lazyListState).value
            } else {
                TopBarMode.Transparent
            }

            TopBar(
                onBackClicked = onBackClicked,
                productName = (productState as? ProductState.Success)?.product?.name,
                onShareClicked = onShareClicked,
                mode = mode,
            )
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding()
            .clipToBounds(),
    ) { padding ->
        val paddingModifier = if (productState is ProductState.Error) {
            Modifier.padding(padding)
        } else {
            Modifier
        }

        ProductDetails(
            productState = productState,
            onBonusCountForPurchaseClicked = { isZarinaClubBottomSheetVisible = true },
            onProductColorClicked = onProductColorClicked,
            onAddProductToCartClicked = onAddProductToCartClicked,
            onAddProductToFavoritesClicked = onAddProductToFavoritesClicked,
            onProductErrorRefreshClicked = onProductErrorRefreshClicked,
            onProductClicked = onProductClicked,
            productTotalLookState = productTotalLookState,
            onProductTotalLookErrorRefreshClicked = onProductTotalLookErrorRefreshClicked,
            productSimilarState = productSimilarState,
            onProductSimilarErrorRefreshClicked = onProductSimilarErrorRefreshClicked,
            onUrlClicked = onUrlClicked,
            lazyListState = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .then(paddingModifier)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
