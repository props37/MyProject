package ru.livetyping.zarina.presentation.common.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaListErrorItem(
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.loading_error),
    contentPadding: PaddingValues = ContentPadding,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
    ) {
        Text(
            text = text,
            style = UiKitTheme.typography.primary.regular,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        ZarinaButton(
            onClick = onRetryClicked,
            size = ZarinaButtonSize.Medium,
        ) {
            Text(text = stringResource(R.string.repeat).uppercase())
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaListErrorItem(
            onRetryClicked = {},
            modifier = Modifier.background(Color.White),
        )
    }
}

private val ContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
