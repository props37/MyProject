package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.model.DateTimeItem
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.model.DateTimeSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.ui.ContinueButton
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.ui.DateTimeItems
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.ui.TopBar

@Composable
internal fun DeliveryOptionDateTimeSelectorScreen(
    navActions: DeliveryOptionDateTimeSelectorNavActions,
    viewModel: DeliveryOptionDateTimeSelectorViewModel = hiltViewModel(),
) {
    val dateTimeSelectorState by viewModel.dateTimeSelectorState.collectAsStateWithLifecycle()

    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        dateTimeSelectorState = dateTimeSelectorState,
        onDateTimeItemClicked = viewModel::onDateTimeItemClicked,
        onContinueClicked = viewModel::onContinueClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    dateTimeSelectorState: DateTimeSelectorState,
    onDateTimeItemClicked: (DateTimeItem) -> Unit,
    onContinueClicked: () -> Unit,
    sideEffects: Flow<DeliveryOptionDateTimeSelectorSideEffect>,
    navActions: DeliveryOptionDateTimeSelectorNavActions,
) {
    DeliveryOptionDateTimeSelectorScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(
            selectorType = dateTimeSelectorState.selectorType,
            onBackClicked = onBackClicked,
        )

        DateTimeItems(
            items = dateTimeSelectorState.dateTimeItems,
            onItemClicked = onDateTimeItemClicked,
            modifier = Modifier.weight(1f),
        )

        ContinueButton(
            isVisible = dateTimeSelectorState.isContinueButtonVisible,
            onClick = onContinueClicked,
        )
    }
}
