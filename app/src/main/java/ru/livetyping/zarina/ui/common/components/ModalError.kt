package ru.livetyping.zarina.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.base.text.textString
import ru.livetyping.zarina.ui.common.base.ErrorStateOld
import ru.livetyping.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.ui.theme.old.ZarinaTheme

@Composable
fun ModalError(
    state: ErrorStateOld,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        ErrorContent(
            state = state
        )
        Spacer(modifier = Modifier.height(12.dp))
        Spacer(modifier = Modifier.weight(1f))
        RefreshButton(
            state = state,
            onClick = onButtonClick
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ErrorContent(
    state: ErrorStateOld,
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
                text = textString(state.title),
                style = UiKitTheme.typographyOld.circle1720bold,
                color = UiKitTheme.colorsOld.primaryContentColor,
                textAlign = TextAlign.Center,
            )
        if (state.subtitle != null)
            Text(
                text = textString(state.subtitle),
                style = UiKitTheme.typographyOld.circle1518,
                color = UiKitTheme.colorsOld.primaryContentColor,
                textAlign = TextAlign.Center,
            )
    }
}

@Composable
private fun RefreshButton(
    state: ErrorStateOld,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isButtonVisible && state.buttonText != null)
        ZarinaTextButton(
            text = textString(state.buttonText),
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
}

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
            state = ErrorStateOld(
                icon = R.drawable.old_ic_no_network_96,
                title = Text.String("Error title"),
                subtitle = Text.String("Error subtitle"),
                isButtonVisible = true,
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
