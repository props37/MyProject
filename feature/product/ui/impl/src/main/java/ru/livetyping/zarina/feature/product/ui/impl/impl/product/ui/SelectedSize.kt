package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SelectedSize(
    title: String,
    selectedSize: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 48.dp)
            .border(
                width = 1.dp,
                color = UiKitTheme2.colors.gray,
                shape = RoundedCornerShape(1.dp),
            )
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 8.dp, end = 12.dp, bottom = 8.dp),
    ) {
        val textStyle = UiKitTheme2.typography.body

        Text(
            text = title.uppercase(),
            style = textStyle,
            color = UiKitTheme2.colors.middleGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = selectedSize.orEmpty().uppercase(),
            style = textStyle,
            color = UiKitTheme2.colors.mainBlack,
            maxLines = 1,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
            contentDescription = title,
            tint = UiKitTheme2.colors.mainBlack,
            modifier = Modifier
                .size(16.dp)
                .rotate(180f),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        SelectedSize(
            title = "Размер".uppercase(),
            selectedSize = "XS (42)",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
