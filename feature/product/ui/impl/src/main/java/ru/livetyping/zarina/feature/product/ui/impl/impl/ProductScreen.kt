package ru.livetyping.zarina.feature.product.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.uicomponent.sizeselector.SizeSelectorEvent
import ru.livetyping.zarina.core.uicomponent.sizeselector.SizeSelectorModalBottomSheet
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaClubModalBottomSheet
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.impl.impl.component.Product
import ru.livetyping.zarina.feature.product.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.product.ui.impl.impl.component.topBarModeAsState
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.ProductState
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarMode
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarState

@Composable
internal fun ProductScreen(
    navActions: ProductFeature.NavActions,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val productState by viewModel.productState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        productState = productState,
        onProductEvent = viewModel::onProductEvent,
        visibleProductSizeSelector = viewModel.visibleProductSizeSelector.collectAsStateWithLifecycle().value,
        onSizeSelectorEvent = viewModel::onSizeSelectorEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

// TODO: [Top] Add TotalLook and similar products

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    topBarState: TopBarState,
    onTopBarEvent: (TopBarEvent) -> Unit,
    productState: ProductState,
    onProductEvent: (ProductEvent) -> Unit,
    visibleProductSizeSelector: Product?,
    onSizeSelectorEvent: (SizeSelectorEvent) -> Unit,
    sideEffects: Flow<ProductSideEffect>,
    navActions: ProductFeature.NavActions,
) {
    ProductScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    if (visibleProductSizeSelector != null) {
        SizeSelectorModalBottomSheet(
            product = visibleProductSizeSelector,
            onEvent = onSizeSelectorEvent,
        )
    }

    var isZarinaClubDescriptionVisible by remember { mutableStateOf(false) }
    if (isZarinaClubDescriptionVisible) {
        ZarinaClubModalBottomSheet(
            onDismissRequest = { isZarinaClubDescriptionVisible = false },
        )
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
                state = topBarState,
                onEvent = onTopBarEvent,
                mode = mode,
                windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
            )
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .bottomNavBarPadding()
            .clipToBounds(),
    ) { padding ->
        val paddingModifier = if (productState is ProductState.Error) {
            Modifier.padding(padding)
        } else {
            Modifier
        }

        Product(
            productState = productState,
            onProductEvent = onProductEvent,
            onShowZarinaClubDescription = { isZarinaClubDescriptionVisible = true },
            lazyListState = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .then(paddingModifier)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        )
    }
}
