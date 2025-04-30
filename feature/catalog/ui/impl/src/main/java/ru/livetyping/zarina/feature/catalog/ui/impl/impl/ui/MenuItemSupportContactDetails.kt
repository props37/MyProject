package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.platform.dialPhoneNumber
import ru.livetyping.zarina.core.uicompose.rememberFormattedPhoneNumber
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.item.ZarinaItemDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.feature.catalog.ui.impl.R

@Composable
internal fun MenuItemSupportContactDetails(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ZarinaItemDefaults.ContentPadding,
) {
    val supportPhone = PhoneNumber.ZARINA_SUPPORT
    val formattedPhone = rememberFormattedPhoneNumber(supportPhone.value)

    if (formattedPhone != null) {
        val context = LocalContext.current

        ZarinaItem(
            onClick = { context.dialPhoneNumber(supportPhone.value) },
            contentPadding = contentPadding,
            modifier = modifier,
        ) {
            Column {
                Text(
                    text = formattedPhone,
                    style = DefaultTextStyle,
                    color = UiKitTheme2.colors.mainBlack,
                )

                Text(
                    text = stringResource(R.string.catalog_free_call_in_russia).uppercase(),
                    style = DefaultTextStyle,
                    color = UiKitTheme2.colors.middleGray,
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        MenuItemSupportContactDetails(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        )
    }
}

private val DefaultTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.body2
