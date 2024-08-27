package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.icon.ZarinaCheckmarkAnimatedIcon
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorViewModel.Item
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CheckoutCourierDeliveryDateTimeSelectorViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutCourierDeliveryDateTimeSelectorScreen(
    navigate: (CheckoutCourierDeliveryDateTimeSelectorScreenAction) -> Unit,
    viewModel: CheckoutCourierDeliveryDateTimeSelectorViewModel = hiltViewModel(),
) {
    val selectorType by viewModel.selectorType.collectAsStateWithLifecycle()
    val items by viewModel.items.collectAsStateWithLifecycle()
    val isContinueButtonVisible by viewModel.isContinueButtonVisible.collectAsStateWithLifecycle()

    ScreenContent(
        selectorType = selectorType,
        items = items,
        onItemClicked = viewModel::onItemClicked,
        isContinueButtonVisible = isContinueButtonVisible,
        onContinueClicked = viewModel::onContinueClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    selectorType: CourierDeliveryDateTimeSelectorType,
    items: List<Item>,
    onItemClicked: (Item) -> Unit,
    isContinueButtonVisible: Boolean,
    onContinueClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutCourierDeliveryDateTimeSelectorScreenAction) -> Unit,
) {
    CheckoutCourierDeliveryDateTimeSelectorScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(
            selectorType = selectorType,
            onBackClicked = onBackClicked,
        )

        val navigationBarHeight =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val contentPadding = PaddingValues(bottom = navigationBarHeight + 20.dp)

        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.weight(1f),
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> item.dateTimePeriodId.value },
            ) { index, item ->
                ZarinaItem(
                    onClick = { onItemClicked(item) },
                    startContent = {
                        Text(
                            text = item.text,
                            style = UiKitTheme.typography.secondary.light,
                        )
                    },
                    endContent = {
                        ZarinaCheckmarkAnimatedIcon(
                            isVisible = item.isSelected,
                            iconSize = 16.dp,
                        )
                    },
                )

                if (index != items.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isContinueButtonVisible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            Column {
                ZarinaDivider(modifier = Modifier.fillMaxWidth())
                ZarinaButton(
                    onClick = onContinueClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                ) {
                    Text(text = stringResource(R.string.continue_).uppercase())
                }
            }
        }
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
