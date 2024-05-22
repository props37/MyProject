package ru.livetyping.zarina.presentation.common.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.LocalContentColor
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.LocalTextStyle
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = BackgroundColor,
    contentColor: Color = ContentColor,
    contentPadding: PaddingValues = ContentPadding,
    endContent: (@Composable RowScope.() -> Unit)? = null,
    startContent: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides UiKitTheme.typography.primary.regular,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .defaultMinSize(minHeight = MinHeight)
                .background(backgroundColor)
                .clickable(
                    enabled = onClick != null,
                    onClick = { onClick?.invoke() },
                )
                .padding(contentPadding),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = startContent,
                modifier = Modifier.weight(1f),
            )
            endContent?.let { content ->
                Spacer(modifier = Modifier.width(16.dp))
                content()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaItem(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Помощь")
        }
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ContentColor: Color
    @Composable
    get() = UiKitTheme.colors.text.general.regular.default

private val ContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

private val MinHeight: Dp get() = 56.dp
