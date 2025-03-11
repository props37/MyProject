package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uicompose.toComposeColor
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.product.ProductPrice
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun ProductGeneralInfo(
    product: ProductDetailed,
    onProductColorClicked: (ProductColor) -> Unit,
    onBonusAccrualForPurchaseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = product.name.uppercase(),
                style = UiKitTheme.typography.caption1.regular,
                color = UiKitTheme.colors.text.general.regular.default,
            )

            val label = product.label
            if (label != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label.name.uppercase(),
                    style = UiKitTheme.typography.caption1.bold,
                    color = label.color.toComposeColor()
                        ?: UiKitTheme.colors.text.general.regular.default,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            ProductPrice(
                price = product.price,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            if (product.bonusAccrualForPurchase > 0) {
                Bonuses(
                    bonusCount = product.bonusAccrualForPurchase,
                    onClick = onBonusAccrualForPurchaseClicked,
                )
            }
        }

        ProductColorSelector(
            productId = product.id,
            productColors = product.colors,
            onProductColorClicked = onProductColorClicked,
            contentPadding = PaddingValues(horizontal = 10.dp),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
internal fun ProductGeneralInfoSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ZarinaTextSkeleton(
            textStyle = UiKitTheme.typography.caption1.regular,
            shimmer = shimmer,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(fraction = 0.5f),
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(42.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(48.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(26.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(44.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(12.dp),
            )
        }

        ProductColorSelectorSkeleton(
            shimmer = shimmer,
            modifier = Modifier.padding(start = 12.dp, top = 2.dp),
        )
    }
}

@Composable
private fun Bonuses(
    bonusCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 28.dp)
            .clip(ZarinaButtonDefaults.Shape)
            .clickable(onClick = onClick),
    ) {
        val bonusCountString = pluralStringResource(
            id = R.plurals.res_bonus_count,
            count = bonusCount,
            bonusCount.toString(),
        )
        Text(
            text = "+$bonusCountString",
            style = UiKitTheme.typography.caption1.regular,
            color = UiKitTheme.colors.text.general.regular.muted,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_question_mark_shaped_24),
            contentDescription = stringResource(R.string.res_for_zarina_club_members),
            tint = UiKitTheme.colors.icon.regular.muted,
            modifier = Modifier.size(16.dp),
        )
    }
}
