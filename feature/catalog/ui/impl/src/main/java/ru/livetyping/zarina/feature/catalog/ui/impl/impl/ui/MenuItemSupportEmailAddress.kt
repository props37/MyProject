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
import androidx.compose.ui.tooling.preview.Preview
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.platform.composeEmail
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.item.ZarinaItemDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.feature.catalog.ui.impl.R

@Composable
internal fun MenuItemSupportEmailAddress(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ZarinaItemDefaults.ContentPadding,
) {
    val supportEmail = Email.ZARINA_SUPPORT

    val context = LocalContext.current

    ZarinaItem(
        onClick = { context.composeEmail(arrayOf(supportEmail.value), subject = null) },
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        Column {
            Text(
                text = supportEmail.value.uppercase(),
                style = SupportDefaultTextStyle,
                color = UiKitTheme2.colors.mainBlack,
            )

            Text(
                text = stringResource(R.string.catalog_support_email_address_description).uppercase(),
                style = SupportDefaultTextStyle,
                color = UiKitTheme2.colors.middleGray,
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        MenuItemSupportEmailAddress(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        )
    }
}
