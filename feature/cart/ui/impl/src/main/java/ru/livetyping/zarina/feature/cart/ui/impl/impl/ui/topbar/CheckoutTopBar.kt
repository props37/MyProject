package ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.text.textString
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.R

@Composable
internal fun CheckoutTopBar(
    state: CheckoutTopBarState,
    onEvent: (CheckoutTopBarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            if (state.isBackButtonVisible) {
                ZarinaBackIconButton(
                    onClick = { onEvent(CheckoutTopBarEvent.BackClicked) },
                )
            }
        },
        centerContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = textString(state.title).uppercase())

                Text(
                    text = stringResource(
                        id = R.string.cart_checkout_step_number,
                        state.checkoutStep,
                        state.checkoutStepCount,
                    ).uppercase(),
                    style = UiKitTheme2.typography.body2,
                    color = UiKitTheme2.colors.middleGray
                )
            }
        },
        endContent = {
            ZarinaCloseIconButton(
                onClick = { onEvent(CheckoutTopBarEvent.CloseClicked) },
            )
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
