package ru.zarina.zarina.ui.screen.productsubscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.domain.rework.product.currentPrice
import ru.zarina.zarina.ui.common.component.ZarinaCheckbox
import ru.zarina.zarina.ui.common.component.button.BackIconButton
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.common.util.rememberFormattedPrice
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.addStyles
import ru.zarina.zarina.util.compose.addUrlAnnotations
import ru.zarina.zarina.util.library.shimmer.shimmerToggleable
import ru.zarina.zarina.utils.kotlin.capitalize

object ProductSubscriptionScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                BackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.product_subscription),
                    style = UiKitTheme.typographyReworked.primary.regular,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    // TODO: [Low] Extract and rename?
    @Composable
    fun ProductCard(
        product: Product,
        productOffer: ProductOffer,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            Row {
                var isImageShimmerEnabled by remember(product) { mutableStateOf(true) }
                AsyncImage(
                    model = product.media.firstOrNull { it.type == MediaType.IMAGE }?.url?.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onSuccess = { isImageShimmerEnabled = false },
                    modifier = Modifier
                        .height(128.dp)
                        .aspectRatio(ProductCardImageAspectRatio)
                        .shimmerToggleable(rememberSkeletonShimmer(), isImageShimmerEnabled)
                        .background(UiKitTheme.colorsReworked.background.skeleton),
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = product.name.uppercase(),
                        style = UiKitTheme.typographyReworked.caption1.regular,
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SizeText(productOffer = productOffer)
                    Spacer(modifier = Modifier.height(2.dp))
                    ColorText(product = product)
                }
            }

            val price = stringResource(
                id = R.string.price_in_rubles_string,
                rememberFormattedPrice(product.price.currentPrice),
            )
            Text(
                text = price.uppercase(),
                style = UiKitTheme.typographyReworked.secondary.regular,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                modifier = Modifier.align(Alignment.BottomEnd),
            )
        }
    }

    @Composable
    fun Policies(
        areAccepted: Boolean,
        onAcceptedChanged: (Boolean) -> Unit,
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(modifier = modifier) {
            PoliciesText(
                onUrlClicked = onUrlClicked,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            ZarinaCheckbox(
                isChecked = areAccepted,
                onCheckedChanged = onAcceptedChanged,
            )
        }
    }

    @OptIn(ExperimentalTextApi::class)
    @Composable
    fun PoliciesText(
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val baseTextStyle = UiKitTheme.typographyReworked.footnote.light.copy(
            color = UiKitTheme.colorsReworked.text.general.regular.default,
        )
        val boldTextStyle = UiKitTheme.typographyReworked.footnote.bold.copy(
            color = UiKitTheme.colorsReworked.text.general.regular.default,
        )

        val baseText = stringResource(R.string.product_subscription_policies)
        val privacyPolicyText = stringResource(R.string.product_subscription_policies_privacy)
        val onlineStorePolicyText =
            stringResource(R.string.product_subscription_policies_online_store)
        val personalDataPolicyText =
            stringResource(R.string.product_subscription_policies_personal_data)

        val privacyPolicyUrl = stringResource(R.string.product_subscription_policies_privacy_url)
        val onlineStorePolicyUrl =
            stringResource(R.string.product_subscription_policies_online_store_url)
        val personalDataPolicyUrl =
            stringResource(R.string.product_subscription_policies_personal_data_url)

        val text = remember(
            baseTextStyle,
            boldTextStyle,
            baseText,
            privacyPolicyText,
            onlineStorePolicyText,
            personalDataPolicyText,
            privacyPolicyUrl,
            onlineStorePolicyUrl,
            personalDataPolicyUrl,
        ) {
            val clickableTextStyle = boldTextStyle.toSpanStyle()
            val substringToStyles = mapOf(
                privacyPolicyText to listOf(clickableTextStyle),
                onlineStorePolicyText to listOf(clickableTextStyle),
                personalDataPolicyText to listOf(clickableTextStyle),
            )
            val substringToUrlAnnotations = mapOf(
                privacyPolicyText to listOf(UrlAnnotation(privacyPolicyUrl)),
                onlineStorePolicyText to listOf(UrlAnnotation(onlineStorePolicyUrl)),
                personalDataPolicyText to listOf(UrlAnnotation(personalDataPolicyUrl)),
            )

            buildAnnotatedString {
                withStyle(baseTextStyle.toSpanStyle()) {
                    append(baseText)
                }
                addStyles(substringToStyles)
                addUrlAnnotations(substringToUrlAnnotations)
            }
        }

        ClickableText(
            text = text,
            onClick = { offset ->
                val annotation = text.getUrlAnnotations(offset, offset).firstOrNull()
                if (annotation != null) {
                    val url = Url(annotation.item.url)
                    onUrlClicked(url)
                }
            },
            modifier = modifier,
        )
    }

    // TODO: [High] Refactor to a single annotated string?
    @Composable
    private fun SizeText(
        productOffer: ProductOffer,
        modifier: Modifier = Modifier,
    ) {
        val textStyle = UiKitTheme.typographyReworked.footnote.regular

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Text(
                text = stringResource(R.string.size),
                style = textStyle,
                color = UiKitTheme.colorsReworked.text.general.regular.disabled,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = productOffer.size + productOffer.sizeRu.orEmpty(),
                style = textStyle,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
            )

            if (productOffer.height != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "|",
                    style = textStyle,
                    color = UiKitTheme.colorsReworked.text.general.regular.disabled,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.height_cm, productOffer.height),
                    style = textStyle,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
            }
        }
    }

    // TODO: [High] Refactor to a single annotated string?
    @Composable
    private fun ColorText(
        product: Product,
        modifier: Modifier = Modifier,
    ) {
        val textStyle = UiKitTheme.typographyReworked.footnote.regular

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Text(
                text = stringResource(R.string.color),
                style = textStyle,
                color = UiKitTheme.colorsReworked.text.general.regular.disabled,
            )
            Spacer(modifier = Modifier.width(8.dp))

            val color = remember(product) {
                product.colors.find { it.productId == product.id }
            }
            Text(
                text = color?.name?.capitalize().orEmpty(),
                style = textStyle,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
            )
        }
    }

    private const val ProductCardImageAspectRatio = 0.7f
}
