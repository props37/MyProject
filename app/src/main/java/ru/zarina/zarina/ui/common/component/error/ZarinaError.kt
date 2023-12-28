package ru.zarina.zarina.ui.common.component.error

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
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme
import java.io.IOException

@Composable
fun ZarinaError(
    type: ZarinaErrorType,
    onRefreshClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconResId: Int
    val titleResId: Int
    val bodyResId: Int
    when (type) {
        ZarinaErrorType.Network -> {
            iconResId = R.drawable.ic_wifi_error_24
            titleResId = R.string.connection_error_title
            bodyResId = R.string.connection_error_body
        }

        ZarinaErrorType.Unknown -> {
            iconResId = R.drawable.ic_heart_broken_24
            titleResId = R.string.something_went_wrong
            bodyResId = R.string.refresh_page_or_come_back_later
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = UiKitTheme.colorsReworked.icon.regular.disabled,
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(titleResId),
            style = UiKitTheme.typographyReworked.primary.bold,
            color = UiKitTheme.colorsReworked.text.general.regular.default,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(bodyResId),
            style = UiKitTheme.typographyReworked.secondary.regular,
            color = UiKitTheme.colorsReworked.text.general.regular.default,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        ZarinaButton(
            onClick = onRefreshClicked,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.refresh).uppercase())
        }
    }
}

enum class ZarinaErrorType {
    Network,
    Unknown;

    companion object {
        fun fromThrowable(throwable: Throwable): ZarinaErrorType = when (throwable) {
            is IOException -> Network
            else -> Unknown
        }
    }
}

@Preview
@DensityPreviews
@FontScalePreviews
@Composable
private fun NetworkErrorPreview() {
    ZarinaTheme {
        ZarinaError(
            type = ZarinaErrorType.Network,
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
private fun UnknownErrorPreview() {
    ZarinaTheme {
        ZarinaError(
            type = ZarinaErrorType.Unknown,
            onRefreshClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
