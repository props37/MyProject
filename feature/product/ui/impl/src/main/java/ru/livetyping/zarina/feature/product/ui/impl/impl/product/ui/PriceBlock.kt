package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
                podeliPrice = podeliPrice,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            BonusAccrualForPurchase(bonusAccrualForPurchase = bonusAccrualForPurchase)
        }
    }
}

@Composable
private fun PodeliPrice(
    podeliPrice: PodeliPrice,
    modifier: Modifier = Modifier,
) {
    val color = UiKitTheme2.colors.middleGray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        val formattedPodeliPayment = run {
            val temp = rememberFormattedPrice(podeliPrice.payment)
            stringResource(RCommon.string.res_price_in_rubles, temp)
        }
        val podeliPriceText = stringResource(
            id = R.string.product_podeli_price,
            formattedPodeliPayment,
            podeliPrice.paymentCount
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

// TODO: [Top] Add info popup
@Composable
private fun BonusAccrualForPurchase(
    bonusAccrualForPurchase: Int,
    modifier: Modifier = Modifier,
) {
    val color = UiKitTheme2.colors.middleGray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
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
    }
}
