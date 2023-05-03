package ru.zarina.zarina.ui.common.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ModalError(
    state: ErrorState,
    modifier: Modifier = Modifier,
    onRefreshClick: () -> Unit = {},
) {
    Layout(
        content = {
            ErrorContent(
                state = state,
                modifier = Modifier.layoutId(LayoutId.CONTENT),
            )
            RefreshButton(
                state = state,
                onClick = onRefreshClick,
                modifier = Modifier.layoutId(LayoutId.BUTTON),
            )
        },
        modifier = modifier,
    ) { measurables, constraints ->
        val buttonPlaceable = measurables
            .first { it.layoutId == LayoutId.BUTTON }
            .measure(constraints.copy(minHeight = 0))

        val maxHeightContent = if (constraints.maxHeight == Constraints.Infinity)
            constraints.maxHeight
        else
            (constraints.maxHeight - buttonPlaceable.height).coerceAtLeast(0)

        val contentPlaceable = measurables
            .first { it.layoutId == LayoutId.CONTENT }
            .measure(constraints.copy(minHeight = 0, maxHeight = maxHeightContent))

        layout(
            constraints.maxWidth,
            constraints.maxHeight,
        ) {
            contentPlaceable.placeRelative(
                x = (constraints.maxWidth - contentPlaceable.width) / 2,
                y = (constraints.maxHeight - contentPlaceable.height) / 2
            )

            buttonPlaceable.placeRelative(
                x = (constraints.maxWidth - buttonPlaceable.width) / 2,
                y = constraints.maxHeight - buttonPlaceable.height
            )
        }
    }
}

@Composable
private fun ErrorContent(
    state: ErrorState,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        if (state.icon != null)
            Image(
                painter = painterResource(state.icon),
                contentDescription = null,
            )
        if (state.title != null)
            Text(
                text = state.title,
                style = UiKitTheme.typography.errorPlaceholderTitle,
                color = UiKitTheme.colors.primaryContentColor,
                textAlign = TextAlign.Center,
            )
        if (state.subtitle != null)
            Text(
                text = state.subtitle,
                style = UiKitTheme.typography.errorPlaceholderBody,
                color = UiKitTheme.colors.primaryContentColor,
                textAlign = TextAlign.Center,
            )
    }
}

@Composable
private fun RefreshButton(
    state: ErrorState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isRefreshButtonVisible)
        ZarinaTextButton(
            text = stringResource(id = R.string.refresh),
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
}

data class ErrorState(
    @DrawableRes
    val icon: Int?,
    val title: String?,
    val subtitle: String?,
    val isRefreshButtonVisible: Boolean = false,
)

private enum class LayoutId { CONTENT, BUTTON }

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@DensityPreviews
@FontScalePreviews
@Composable
fun ModalErrorPreview() {
    ZarinaTheme {
        ModalError(
            state = ErrorState(
                icon = R.drawable.ic_no_network_96,
                title = "Error title",
                subtitle = "Error subtitle",
                isRefreshButtonVisible = true,
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
