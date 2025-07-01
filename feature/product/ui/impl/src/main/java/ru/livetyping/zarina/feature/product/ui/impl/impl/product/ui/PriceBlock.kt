package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.PodeliPrice
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import ru.livetyping.zarina.core.uicompose.price.rememberFormattedPrice
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun PriceBlock(
    price: ProductPrice,
    podeliPrice: PodeliPrice,
    onPodeliPriceClicked: () -> Unit,
    bonusAccrualForPurchase: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row {
            val formattedCurrentPrice = rememberFormattedPrice(price.currentPrice)
            Text(
                text = stringResource(
                    id = RCommon.string.res_price_in_rubles,
                    formattedCurrentPrice
                ).uppercase(),
                style = UiKitTheme2.typography.h2Regular,
                color = UiKitTheme2.colors.mainBlack,
                modifier = Modifier.alignByBaseline(),
            )

            if (price.discount != null) {
                Spacer(modifier = Modifier.width(10.dp))

                val formattedOriginalPrice = rememberFormattedPrice(price.originalPrice)
                Text(
                    text = stringResource(
                        id = RCommon.string.res_price_in_rubles,
                        formattedOriginalPrice,
                    ).uppercase(),
                    style = UiKitTheme2.typography.body2,
                    color = UiKitTheme2.colors.middleGray,
                    textDecoration = TextDecoration.LineThrough,
                    modifier = Modifier.alignByBaseline(),
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            PodeliPrice(
                price = podeliPrice,
                onClick = onPodeliPriceClicked,
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))

            BonusAccrualForPurchase(bonusAccrualForPurchase = bonusAccrualForPurchase)
        }
    }
}

@Composable
private fun PodeliPrice(
    price: PodeliPrice,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = UiKitTheme2.colors.middleGray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(role = Role.Button, onClick = onClick),
    ) {
        val formattedPodeliPayment = run {
            val temp = rememberFormattedPrice(price.payment)
            stringResource(RCommon.string.res_price_in_rubles, temp)
        }
        val podeliPriceText = stringResource(
            id = R.string.product_podeli_price,
            formattedPodeliPayment,
            price.paymentCount
        )

        Text(
            text = podeliPriceText.uppercase(),
            style = UiKitTheme2.typography.body2,
            color = color,
        )

        Spacer(modifier = Modifier.width(1.dp))

        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .padding(bottom = 2.dp)
                .size(12.dp)
                .rotate(90f),
        )
    }
}

@Composable
private fun BonusAccrualForPurchase(
    bonusAccrualForPurchase: Int,
    modifier: Modifier = Modifier,
) {
    var isZarinaClubPopupVisible by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(
            interactionSource = null,
            indication = null,
            role = Role.Button,
            onClick = { isZarinaClubPopupVisible = true },
        ),
    ) {
        val color = UiKitTheme2.colors.middleGray

        val text = pluralStringResource(
            id = RCommon.plurals.res_bonus_count,
            count = bonusAccrualForPurchase,
            bonusAccrualForPurchase.toString(),
        )

        Text(
            text = "+$text".uppercase(),
            style = UiKitTheme2.typography.body2,
            color = color,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_question_mark_shaped_24),
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .padding(bottom = 2.dp)
                .size(12.dp),
        )

        ZarinaClubPopup(
            isVisible = isZarinaClubPopupVisible,
            onDismissRequest = { isZarinaClubPopupVisible = false },
        )
    }
}
