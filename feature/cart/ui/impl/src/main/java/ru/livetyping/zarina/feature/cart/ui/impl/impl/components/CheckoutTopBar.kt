package ru.livetyping.zarina.feature.cart.ui.impl.impl.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.model.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.model.CheckoutTopBarState

@Composable
internal fun CheckoutTopBar(
    state: CheckoutTopBarState,
    title: String,
    onEvent: (CheckoutTopBarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            if (state.isBackButtonVisible) {
                ZarinaBackIconButton(
                    onClick = { onEvent(CheckoutTopBarEvent.BackClicked) },
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            }
        },
        centerContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = title)

                Text(
                    text = stringResource(
                        id = R.string.cart_checkout_step_number,
                        state.checkoutStep,
                        state.checkoutStepCount,
                    ),
                    style = UiKitTheme.typography.tertiary.light,
                )
            }
        },
        endContent = {
            ZarinaCloseIconButton(
                onClick = { onEvent(CheckoutTopBarEvent.CloseClicked) },
                iconSize = 20.dp,
                modifier = Modifier.padding(end = 2.dp),
            )
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
