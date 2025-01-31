package ru.livetyping.zarina.core.uikit.sizeselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SizeSelectorHeightList(
    heights: List<ProductOffer>,
    onHeightClicked: (ProductOffer) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        heights.forEachIndexed { index, offer ->
            key(offer.id.value) {
                Height(
                    height = offer,
                    onClick = { onHeightClicked(offer) },
                )

                if (index < heights.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Height(
    height: ProductOffer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = onClick,
        startContent = {
            val heightValue = height.height
            val text = if (heightValue != null) {
                stringResource(RCommon.string.res_height_cm, heightValue)
            } else ""
            val color = if (height.isAvailable) {
                UiKitTheme.colors.text.general.regular.default
            } else {
                UiKitTheme.colors.text.general.regular.disabled
            }

            Text(
                text = text,
                style = SizeSelectorDefaults.SizeMainTextStyle,
                color = color,
            )
        },
        endContent = {
            if (!height.isAvailable) {
                Text(
                    text = stringResource(RCommon.string.res_subscribe).uppercase(),
                    style = SizeSelectorDefaults.SizeAdditionalTextStyle,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            }
        },
        modifier = modifier,
    )
}
