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

@Composable
internal fun CheckoutTopBar(
    title: String,
    step: Int,
    stepCount: Int,
    isBackButtonVisible: Boolean,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClicked: (() -> Unit)? = null,
) {
    ZarinaTopBar(
        startContent = {
            if (isBackButtonVisible) {
                ZarinaBackIconButton(
                    onClick = { onBackClicked?.invoke() },
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            }
        },
        centerContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = title)

                Text(
                    text = stringResource(R.string.cart_checkout_step_number, step, stepCount),
                    style = UiKitTheme.typography.tertiary.light,
                )
            }
        },
        endContent = {
            ZarinaCloseIconButton(
                onClick = onCloseClicked,
                iconSize = 20.dp,
                modifier = Modifier.padding(end = 2.dp),
            )
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
