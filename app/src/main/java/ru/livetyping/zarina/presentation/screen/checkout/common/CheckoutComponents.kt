package ru.livetyping.zarina.presentation.screen.checkout.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.theme.UiKitTheme

object CheckoutComponents {

    @Composable
    fun TopBar(
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
                        text = stringResource(R.string.step_number, step, stepCount),
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
}
