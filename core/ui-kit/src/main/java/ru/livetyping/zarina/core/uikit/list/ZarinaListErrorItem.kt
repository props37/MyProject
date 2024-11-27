package ru.livetyping.zarina.core.uikit.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun ZarinaListErrorItem(
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(RCommon.string.res_loading_error),
    contentPadding: PaddingValues = ZarinaListErrorItemDefaults.ContentPadding,
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
            Text(text = stringResource(RCommon.string.res_repeat).uppercase())
        }
    }
}

public object ZarinaListErrorItemDefaults {
    internal val ContentPadding: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
}
