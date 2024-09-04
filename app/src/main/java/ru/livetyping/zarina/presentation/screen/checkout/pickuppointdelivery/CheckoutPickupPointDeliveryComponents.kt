package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.PickupPointsState
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.ViewMode
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

@Suppress("ConstPropertyName")
object CheckoutPickupPointDeliveryComponents {

    @Composable
    fun FilterBlock(
        nameOrAddressFilterTextFieldState: TextFieldState,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            var focusState by remember { mutableStateOf<FocusState?>(null) }
            ZarinaTextField(
                state = nameOrAddressFilterTextFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                leadingContent = {
                   Icon(
                       imageVector = ImageVector.vectorResource(R.drawable.ic_magnifying_glass_24),
                       contentDescription = null,
                       modifier = Modifier.size(16.dp),
                   )
                },
                placeholder = {
                    Text(text = stringResource(R.string.address_or_name))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = nameOrAddressFilterTextFieldState.text.isNotBlank(),
                        onClick = { nameOrAddressFilterTextFieldState.clearText() },
                    )
                },
                outerTrailingContent = {
                    val focusManager = LocalFocusManager.current
                    ZarinaTextFieldDefaults.CancelButton(
                        isVisible = focusState?.isFocused == true,
                        onClick = { focusManager.clearFocus() },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onFocusChanged { focusState = it },
            )
        }
    }

    @Composable
    fun ViewModeTabRow(
        modes: List<ViewMode>,
        currentMode: ViewMode,
        onModeChanged: (ViewMode) -> Unit,
        onCurrentModeClicked: (ViewMode) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTabRow(
            selectedTabIndex = modes.indexOf(currentMode),
            modifier = modifier,
        ) {
            modes.forEach { mode ->
                val textResId = when (mode) {
                    ViewMode.MAP -> R.string.map
                    ViewMode.LIST -> R.string.list
                }

                ZarinaTab(
                    text = stringResource(textResId),
                    isSelected = mode == currentMode,
                    onClick = {
                        if (mode == currentMode) {
                            onCurrentModeClicked(mode)
                        } else {
                            onModeChanged(mode)
                        }
                    },
                )
            }
        }
    }

    @Composable
    fun ViewModeHorizontalPager(
        pagerState: PagerState,
        viewModes: List<ViewMode>,
        pickupPointsState: PickupPointsState,
        onPickupPointClicked: (PickupPoint) -> Unit,
        pickupPointsListState: LazyListState,
        onPickupPointsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            when (viewModes[page]) {
                ViewMode.MAP -> {
                    // TODO: [High] Implement
                }

                ViewMode.LIST -> {
                    PickupPointStateScaffold(
                        pickupPointsState = pickupPointsState,
                        onErrorRefreshClicked = onPickupPointsErrorRefreshClicked,
                        successContent = {
                            PickupPointList(
                                pickupPointsState = it,
                                onPickupPointClicked = onPickupPointClicked,
                                lazyListState = pickupPointsListState,
                            )
                        },
                        loadingContent = {
                            PickupPointListSkeleton()
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }

    @Composable
    private fun PickupPointStateScaffold(
        pickupPointsState: PickupPointsState,
        onErrorRefreshClicked: () -> Unit,
        successContent: @Composable (PickupPointsState.Success) -> Unit,
        loadingContent: @Composable () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = pickupPointsState,
            contentKey = {
                when (it) {
                    is PickupPointsState.Success -> PickupPointStateScaffoldContentKeySuccess
                    is PickupPointsState.Error -> it
                    PickupPointsState.Loading -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is PickupPointsState.Success -> {
                    successContent(state)
                }

                PickupPointsState.Loading -> {
                    loadingContent()
                }

                is PickupPointsState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun PickupPointList(
        pickupPointsState: PickupPointsState.Success,
        onPickupPointClicked: (PickupPoint) -> Unit,
        lazyListState: LazyListState,
        modifier: Modifier = Modifier,
    ) {
        val navigationBarHeight =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val contentPadding = PaddingValues(bottom = 20.dp + navigationBarHeight)
        LazyColumn(
            state = lazyListState,
            contentPadding = contentPadding,
            modifier = modifier,
        ) {
            itemsIndexed(
                items = pickupPointsState.pickupPoints,
                key = { _, pickupPoint -> pickupPoint.id.value },
            ) { index, pickupPoint ->
                Column(modifier = Modifier.animateItem()) {
                    PickupPoint(
                        pickupPoint = pickupPoint,
                        onClick = { onPickupPointClicked(pickupPoint) },
                    )

                    if (index < pickupPointsState.pickupPoints.lastIndex) {
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

    @Composable
    private fun PickupPointListSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer(bounds = ShimmerBounds.Window)
        Column {
            repeat(PickupPointListSkeletonCount) { index ->
                PickupPointSkeleton(shimmer = shimmer)

                if (index < PickupPointListSkeletonCount - 1) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }

            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(20.dp + navigationBarHeight))
        }
    }

    @Composable
    private fun PickupPoint(
        pickupPoint: PickupPoint,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            onClick = onClick,
            startContent = {
                Column {
                    Text(
                        text = pickupPoint.title,
                        style = UiKitTheme.typography.secondary.light,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = pickupPoint.title,
                        style = UiKitTheme.typography.footnote.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }
            },
            endContent = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(degrees = 90f),
                )
            },
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            modifier = modifier,
        )
    }

    @Composable
    private fun PickupPointSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            modifier = modifier,
        ) {
            Column {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.secondary.light,
                    modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                    shimmer = shimmer,
                )
                Spacer(modifier = Modifier.height(4.dp))
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.footnote.light,
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                    shimmer = shimmer,
                )
            }
        }
    }

    private const val PickupPointStateScaffoldContentKeySuccess =
        "PickupPointStateScaffoldContentKeySuccess"

    private const val PickupPointListSkeletonCount = 8
}
