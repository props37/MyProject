package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.ProductHeight
import ru.livetyping.zarina.core.domain.model.product.ProductSizeFull
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SizeInfo(
    sizeOnModel: String?,
    onSizeTableClicked: () -> Unit,
    selectedSize: ProductSizeFull?,
    selectedHeight: ProductHeight?,
    isHeightSelectorVisible: Boolean,
    onSelectedSizeClicked: () -> Unit,
    onSelectedHeightClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (sizeOnModel != null) {
                SizeOnModel(sizeOnModel = sizeOnModel)
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))

            SizeTableButton(onClick = onSizeTableClicked)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            SelectedSize(
                title = stringResource(RCommon.string.res_size).uppercase(),
                selectedSize = selectedSize?.size,
                onClick = onSelectedSizeClicked,
                modifier = Modifier.weight(1f),
            )

            if (selectedHeight != null && isHeightSelectorVisible) {
                Spacer(modifier = Modifier.width(8.dp))

                SelectedSize(
                    title = stringResource(RCommon.string.res_height).uppercase(),
                    selectedSize = selectedHeight.height,
                    onClick = onSelectedHeightClicked,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun SizeOnModel(
    sizeOnModel: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(UiKitTheme2.colors.lightGray, CircleShape)
            .padding(start = 12.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        val textStyle = UiKitTheme2.typography.body2

        Text(
            text = stringResource(R.string.product_model_wearing_size).uppercase(),
            style = textStyle,
            color = UiKitTheme2.colors.middleGray,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = sizeOnModel.uppercase(),
            style = textStyle,
            color = UiKitTheme2.colors.mainBlack,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
            contentDescription = null,
            tint = UiKitTheme2.colors.mainBlack,
            modifier = Modifier
                .padding(bottom = 2.dp)
                .size(12.dp)
                .rotate(90f),
        )
    }
}

@Composable
private fun SizeTableButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(min = 40.dp)
            .clip(RoundedCornerShape(1.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = stringResource(RCommon.string.res_size_table).uppercase(),
            style = UiKitTheme2.typography.body2,
            color = UiKitTheme2.colors.mainBlack,
            textDecoration = TextDecoration.Underline,
        )
    }
}
