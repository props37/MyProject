package ru.zarina.zarina.ui.common.component.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.base.textString
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec

@Composable
fun ZarinaErrorScreen(
    state: ErrorState,
    onRefreshClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(state.iconResId),
            contentDescription = null,
            tint = UiKitTheme.colors.icon.regular.disabled,
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = textString(state.title),
            style = UiKitTheme.typography.primary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = textString(state.body),
            style = UiKitTheme.typography.secondary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            targetState = state.isRefreshButtonVisible,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
            },
            label = "ZarinaErrorScreen Refresh button",
        ) { isVisible ->
            if (isVisible) {
                ZarinaButton(
                    onClick = onRefreshClicked,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(R.string.refresh).uppercase())
                }
            }
        }
    }
}

@Preview
@DensityPreviews
@FontScalePreviews
@Composable
private fun NetworkErrorPreview() {
    ZarinaTheme {
        ZarinaErrorScreen(
            state = ErrorState.NETWORK,
            onRefreshClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}

@Preview
@DensityPreviews
@FontScalePreviews
@Composable
private fun GenericErrorPreview() {
    ZarinaTheme {
        ZarinaErrorScreen(
            state = ErrorState.GENERIC,
            onRefreshClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
