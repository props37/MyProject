package ru.livetyping.zarina.ui.common.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.domain.user.LoyaltyCardLevel
import ru.livetyping.zarina.ui.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.ui.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.common.util.domain.nameResId
import ru.livetyping.zarina.ui.common.util.rememberFormattedPrice
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun LoyaltyCard(
    card: LoyaltyCard,
    onShowInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
    initialSide: LoyaltyCardSide = LoyaltyCardSide.FRONT,
    onTurned: ((LoyaltyCardSide) -> Unit)? = null,
) {
    val contentColor by animateColorAsState(
        targetValue = when (card.level) {
            LoyaltyCardLevel.PRIME, LoyaltyCardLevel.PRIORITY -> {
                UiKitTheme.colors.text.general.regular.default
            }

            LoyaltyCardLevel.STAR -> UiKitTheme.colors.text.general.inversed.default
        },
        label = "contentColor",
    )

    // TODO: [High] Test!
    var side by rememberSaveable { mutableStateOf(initialSide) }
    val rotation = animateFloatAsState(
        targetValue = when (side) {
            LoyaltyCardSide.FRONT -> ROTATION_FRONT_SIDE
            LoyaltyCardSide.BACK -> ROTATION_BACK_SIDE
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "rotation",
    )
    val visibleSide by remember {
        derivedStateOf {
            if (rotation.value <= ROTATION_BACK_SIDE / 2) LoyaltyCardSide.FRONT else LoyaltyCardSide.BACK
        }
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = modifier
                .graphicsLayer {
                    shape = ShapeDefault
                    clip = true
                    rotationY = rotation.value
                },
        ) {
            Background(
                level = card.level,
                modifier = Modifier.matchParentSize(),
            )

            FrontSide(
                card = card,
                onShowBackSideClicked = { side = LoyaltyCardSide.BACK },
                onShowInfoClicked = onShowInfoClicked,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = if (visibleSide == LoyaltyCardSide.FRONT) 1f else 0f
                    },
            )

            BackSide(
                card = card,
                onShowFrontSideClicked = { side = LoyaltyCardSide.FRONT },
                modifier = Modifier
                    .graphicsLayer {
                        alpha = if (visibleSide == LoyaltyCardSide.BACK) 1f else 0f
                        rotationY = ROTATION_BACK_SIDE
                    },
            )
        }
    }
}

// TODO: [High] Add progress bar
@Composable
private fun FrontSide(
    card: LoyaltyCard,
    onShowBackSideClicked: () -> Unit,
    onShowInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    Column(modifier = modifier.padding(ContentPaddingFrontSide)) {
        Row {
            Text(
                text = card.bonuses.bonusCount.toString(),
                style = UiKitTheme.typography.heading1.regular,
            )
            Spacer(modifier = Modifier.width(6.dp))
            val topPadding = with(density) { 6.sp.toDp() }
            Text(
                text = pluralStringResource(
                    id = R.plurals.bonuses,
                    count = card.bonuses.bonusCount
                ),
                style = UiKitTheme.typography.tertiary.regular.copy(
                    lineHeight = UiKitTheme.typography.heading1.regular.lineHeight,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = topPadding),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onShowBackSideClicked,
                    ),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_qr_24),
                    contentDescription = stringResource(R.string.qr_code),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val textStyle = UiKitTheme.typography.caption1.bold
                    Text(
                        text = stringResource(R.string.qr_code).uppercase(),
                        style = textStyle,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    val iconSize = with(density) { textStyle.fontSize.toDp() * 0.8f }
                    Icon(
                        painter = painterResource(R.drawable.ic_small_arrow_up_24),
                        contentDescription = stringResource(R.string.qr_code),
                        modifier = Modifier
                            .padding(bottom = 2.dp) // Circe font padding
                            .size(iconSize)
                            .rotate(degrees = 90f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(card.level.nameResId),
            style = UiKitTheme.typography.primary.bold,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            val formattedRemainingPurchaseSum =
                rememberFormattedPrice(card.nextLevel.remainingPurchaseSum.toLong())
            Text(
                text = stringResource(R.string.to_next_level, formattedRemainingPurchaseSum),
                style = UiKitTheme.typography.tertiary.light,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))

            val iconSize = 16.dp
            val iconColor = LocalContentColor.current
            ZarinaIconButton(
                onClick = onShowInfoClicked,
                indication = rememberRipple(bounded = false, radius = iconSize),
                modifier = Modifier
                    .size(iconSize)
                    .wrapContentSize(unbounded = true),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_exclamation_mark_shaped_24),
                    tint = iconColor,
                    contentDescription = stringResource(R.string.show_loyalty_card_info),
                    modifier = Modifier.size(iconSize),
                )
            }
        }
    }
}

// TODO: [High] Add QR code
@Composable
private fun BackSide(
    card: LoyaltyCard,
    onShowFrontSideClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(ContentPaddingBackSide)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = card.number.value,
                style = UiKitTheme.typography.tertiary.regular,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))

            val iconSize = 16.dp
            val iconColor = LocalContentColor.current
            ZarinaIconButton(
                onClick = onShowFrontSideClicked,
                indication = rememberRipple(bounded = false, radius = iconSize),
                modifier = Modifier
                    .size(iconSize)
                    .wrapContentSize(unbounded = true),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_turn_back_24),
                    tint = iconColor,
                    contentDescription = stringResource(R.string.turn_card),
                    modifier = Modifier.size(iconSize),
                )
            }
        }
    }
}

@Composable
private fun Background(
    level: LoyaltyCardLevel,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.loyalty_card_background_prime),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )

        Crossfade(
            targetState = level,
            label = "background",
            modifier = Modifier.matchParentSize(),
        ) { level ->
            val backgroundResId = when (level) {
                LoyaltyCardLevel.PRIME -> R.drawable.loyalty_card_background_prime
                LoyaltyCardLevel.PRIORITY -> R.drawable.loyalty_card_background_priority
                LoyaltyCardLevel.STAR -> R.drawable.loyalty_card_background_star
            }
            Image(
                painter = painterResource(backgroundResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun PreviewPrime() {
    ZarinaPreview {
        LoyaltyCard(
            card = remember {
                FakeDataGenerator.getLoyaltyCard(
                    level = LoyaltyCardLevel.PRIME,
                )
            },
            onShowInfoClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewPriority() {
    ZarinaPreview {
        LoyaltyCard(
            card = remember {
                FakeDataGenerator.getLoyaltyCard(
                    level = LoyaltyCardLevel.PRIORITY,
                )
            },
            onShowInfoClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewStar() {
    ZarinaPreview {
        LoyaltyCard(
            card = remember {
                FakeDataGenerator.getLoyaltyCard(
                    level = LoyaltyCardLevel.STAR,
                )
            },
            onShowInfoClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewBackSide() {
    ZarinaPreview {
        LoyaltyCard(
            card = remember {
                FakeDataGenerator.getLoyaltyCard(
                    level = LoyaltyCardLevel.STAR,
                )
            },
            initialSide = LoyaltyCardSide.BACK,
            onShowInfoClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

enum class LoyaltyCardSide { FRONT, BACK }

private const val ROTATION_FRONT_SIDE = 0f
private const val ROTATION_BACK_SIDE = 180f

private val ShapeDefault: Shape get() = RoundedCornerShape(12.dp)
private val ContentPaddingFrontSide: PaddingValues get() = PaddingValues(30.dp)
private val ContentPaddingBackSide: PaddingValues get() = PaddingValues(16.dp)
