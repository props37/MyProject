package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SuggestionListError(
    title: String,
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SuggestionListTitle(title)

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .aspectRatio(AspectRatio)
                .background(UiKitTheme2.colors.lightGray)
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.product_failed_to_load_block).uppercase(),
                style = UiKitTheme2.typography.body2,
                color = UiKitTheme2.colors.mainBlack,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            ZarinaIconButton(onClick = onRetryClicked) {
                Icon(
                    imageVector = ImageVector.vectorResource(RCommon.drawable.ic_reload_24),
                    contentDescription = stringResource(RCommon.string.res_reload),
                    tint = UiKitTheme2.colors.mainBlack,
                )
            }
        }
    }
}

private const val AspectRatio = 1.2f
