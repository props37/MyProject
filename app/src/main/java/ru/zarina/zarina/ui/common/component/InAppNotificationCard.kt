package ru.zarina.zarina.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun InAppNotificationCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    content: @Composable RowScope.() -> Unit,
) {
    val shape = RoundedCornerShape(2.dp)

    Row(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape)
            .background(
                color = UiKitTheme.colors.background.general.regular.default,
                shape = shape,
            )
            .padding(contentPadding),
        content = content,
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            InAppNotificationCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            ) {
                Box(modifier = Modifier.height(60.dp))
            }
        }
    }
}
