package ru.livetyping.zarina.ui.common.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import qrcode.QRCode
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.domain.user.LoyaltyCardLevel
import ru.livetyping.zarina.domain.user.contains
import ru.livetyping.zarina.domain.user.requiredPurchaseSum
import ru.livetyping.zarina.ui.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.ui.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.common.util.domain.nameResId
import ru.livetyping.zarina.ui.common.util.rememberFormattedPrice
import ru.livetyping.zarina.ui.theme.UiKitTheme
import timber.log.Timber
import kotlin.enums.EnumEntries

@Composable
fun LoyaltyCard(
    card: LoyaltyCard,
    onLevelInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
    initialSide: LoyaltyCardSide = LoyaltyCardSide.FRONT,
    onSideChanged: ((LoyaltyCardSide) -> Unit)? = null,
) {
    val density = LocalDensity.current

    val contentColor by animateColorAsState(
        targetValue = card.level.contentColor,
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
            if (rotation.value <= ROTATION_TURN_THRESHOLD) LoyaltyCardSide.FRONT else LoyaltyCardSide.BACK
        }
    }

    val updatedOnTurned by rememberUpdatedState(onSideChanged)
    LaunchedEffect(Unit) {
        snapshotFlow { visibleSide }
            .distinctUntilChanged()
            .collect { updatedOnTurned?.invoke(it) }
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = modifier
                .height(IntrinsicSize.Min)
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

            var frontSideHeightDp by remember { mutableStateOf(0.dp) }

            FrontSide(
                card = card,
                onShowBackSideClicked = { side = LoyaltyCardSide.BACK },
                onLevelInfoClicked = onLevelInfoClicked,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = if (visibleSide == LoyaltyCardSide.FRONT) 1f else 0f
                    }
                    .onSizeChanged {
                        frontSideHeightDp = with(density) { it.height.toDp() }
                    },
            )

            BackSide(
                card = card,
                onShowFrontSideClicked = { side = LoyaltyCardSide.FRONT },
                modifier = Modifier
                    .height(frontSideHeightDp)
                    .graphicsLayer {
                        alpha = if (visibleSide == LoyaltyCardSide.BACK) 1f else 0f
                        rotationY = ROTATION_BACK_SIDE
                    },
            )
        }
    }
}

@Composable
private fun FrontSide(
    card: LoyaltyCard,
    onShowBackSideClicked: () -> Unit,
    onLevelInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(ContentPaddingFrontSide)) {
        Row {
            FrontSideBonuses(
                bonuses = card.bonuses,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(16.dp))

            FrontSideQrCode(
                onShowBackSideClicked = onShowBackSideClicked,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FrontSideLevelInfo(
            card = card,
            onLevelInfoClicked = onLevelInfoClicked,
        )

        Spacer(modifier = Modifier.height(16.dp))

        FrontSideProgressBar(card = card)
    }
}

@Composable
private fun BackSide(
    card: LoyaltyCard,
    onShowFrontSideClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ContentPaddingBackSide)
    ) {
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

        Spacer(modifier = Modifier.height(4.dp))

        BackSideQrCode(
            card = card,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun FrontSideBonuses(
    bonuses: LoyaltyCard.Bonuses,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = bonuses.bonusCount.toString(),
            style = UiKitTheme.typography.heading1.regular,
        )
        Spacer(modifier = Modifier.width(6.dp))

        val topPadding = with(LocalDensity.current) { 6.sp.toDp() }
        Text(
            text = pluralStringResource(
                id = R.plurals.bonuses,
                count = bonuses.bonusCount,
            ),
            style = UiKitTheme.typography.tertiary.regular,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = topPadding),
        )
    }
}

@Composable
private fun FrontSideQrCode(
    onShowBackSideClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onShowBackSideClicked,
            ),
    ) {
        val qrCodeIconSize = 24.dp
        Icon(
            painter = painterResource(R.drawable.ic_qr_24),
            contentDescription = stringResource(R.string.qr_code),
            modifier = Modifier
                .size(qrCodeIconSize)
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        bounded = false,
                        radius = qrCodeIconSize - 6.dp,
                    )
                ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            val textStyle = UiKitTheme.typography.caption1.bold
            Text(
                text = stringResource(R.string.qr_code).uppercase(),
                style = textStyle,
            )
            Spacer(modifier = Modifier.width(2.dp))

            @Suppress("MagicNumber")
            val iconSize = with(LocalDensity.current) { textStyle.fontSize.toDp() * 0.8f }
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

@Composable
private fun FrontSideLevelInfo(
    card: LoyaltyCard,
    onLevelInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val levelNameTextStyle = UiKitTheme.typography.primary.bold

    val levelInfoButton = @Composable {
        val iconSize = 16.dp
        val iconColor = LocalContentColor.current
        ZarinaIconButton(
            onClick = onLevelInfoClicked,
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

    // Display both full and short info anyway to keep the card height the same regardless of its level
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier,
    ) {
        val fullInfoAlpha = if (card.nextLevelInfo != null) 1f else 0f
        Column(modifier = Modifier.alpha(fullInfoAlpha)) {
            Text(
                text = stringResource(card.level.nameResId),
                style = levelNameTextStyle,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val remainingPurchaseSum = if (card.nextLevelInfo != null) {
                    card.nextLevelInfo.requiredPurchaseSum - card.totalPurchaseSum
                } else 0
                val formattedRemainingPurchaseSum = stringResource(
                    id = R.string.price_in_rubles_string,
                    rememberFormattedPrice(remainingPurchaseSum.toLong()),
                )
                Text(
                    text = stringResource(R.string.to_next_level, formattedRemainingPurchaseSum),
                    style = UiKitTheme.typography.tertiary.light,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                levelInfoButton()
            }
        }

        val shortInfoAlpha = if (card.nextLevelInfo != null) 0f else 1f
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.alpha(shortInfoAlpha),
        ) {
            Text(
                text = stringResource(card.level.nameResId),
                style = levelNameTextStyle,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            levelInfoButton()
        }
    }
}

@Composable
private fun FrontSideProgressBar(
    card: LoyaltyCard,
    modifier: Modifier = Modifier,
) {
    val trackColor = animateColorAsState(
        targetValue = when (card.level) {
            LoyaltyCardLevel.PRIME, LoyaltyCardLevel.PRIORITY -> {
                UiKitTheme.colors.text.general.inversed.default
            }

            LoyaltyCardLevel.STAR -> UiKitTheme.colors.text.general.regular.default
        },
        label = "trackColor",
    )
    val progressColor = LocalContentColor.current

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(ProgressBarDotSize)
            .clipToBounds(),
    ) {
        val trackWidth = ProgressBarTrackWidth.toPx()
        val dotSize = ProgressBarDotSize.toPx()
        val dotRadius = dotSize / 2

        val levels = LoyaltyCardLevel.entries
        val levelSegmentCount = levels.size - 1
        val levelSegmentWidth = size.width / levelSegmentCount
        val levelToDotCenterX = levels
            .mapIndexed { index, level ->
                val dotCenterX = when (index) {
                    0 -> dotRadius
                    levels.lastIndex -> size.width - dotRadius
                    else -> index * levelSegmentWidth
                }
                level to dotCenterX
            }
            .toMap()

        // Draw track
        val trackSize = Size(width = size.width - dotSize, height = trackWidth)
        drawTrack(
            trackWidth = trackWidth,
            trackSize = trackSize,
            trackColor = trackColor.value,
            dotSize = dotSize,
        )

        // Draw progress
        drawProgress(
            card = card,
            levels = levels,
            levelToDotCenterX = levelToDotCenterX,
            trackWidth = trackWidth,
            trackSize = trackSize,
            progressColor = progressColor,
        )

        // Draw dots
        drawDots(
            cardLevel = card.level,
            levelToDotCenterX = levelToDotCenterX,
            dotRadius = dotRadius,
            trackColor = trackColor.value,
            progressColor = progressColor,
        )
    }
}

@Composable
private fun BackSideQrCode(
    card: LoyaltyCard,
    modifier: Modifier = Modifier,
) {
    val color = card.level.contentColor
    val bitmap: Bitmap? = remember(card.number, color) {
        val byteArray = QRCode.ofSquares()
            .withBackgroundColor(Color.Transparent.toArgb())
            .withColor(color.toArgb())
            .withInnerSpacing(0)
            .build(card.number.value)
            .render()
            .getBytes()
        Timber.tag(Tag).v("QR code generated")
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    DisposableEffect(bitmap) {
        onDispose {
            bitmap?.recycle()
            Timber.tag(Tag).v("QR code bitmap recycled")
        }
    }
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxHeight(),
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = stringResource(R.string.qr_code),
                contentScale = ContentScale.FillHeight,
            )
        } else {
            Text(
                text = stringResource(R.string.qr_code_generation_error),
                style = UiKitTheme.typography.secondary.regular,
                textAlign = TextAlign.Center,
            )
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

private fun DrawScope.drawTrack(
    trackWidth: Float,
    trackSize: Size,
    trackColor: Color,
    dotSize: Float,
) {
    val trackTopLeft = Offset(x = dotSize / 2, y = (size.height - trackWidth) / 2)
    drawRect(
        color = trackColor,
        topLeft = trackTopLeft,
        size = trackSize,
    )
}

private fun DrawScope.drawProgress(
    card: LoyaltyCard,
    levels: EnumEntries<LoyaltyCardLevel>,
    levelToDotCenterX: Map<LoyaltyCardLevel, Float>,
    trackWidth: Float,
    trackSize: Size,
    progressColor: Color,
) {
    levels
        .windowed(size = 2)
        .forEach { levelPair ->
            val startLevel = levelPair[0]
            val endLevel = levelPair[1]

            when {
                endLevel in card.level -> {
                    val startLevelDotCenterX = levelToDotCenterX[startLevel] ?: 0f
                    val endLevelDotCenterX = levelToDotCenterX[endLevel] ?: 0f
                    val topLeft = Offset(
                        x = startLevelDotCenterX,
                        y = (size.height - trackWidth) / 2,
                    )
                    val size = Size(
                        width = endLevelDotCenterX - startLevelDotCenterX,
                        height = trackSize.height,
                    )
                    drawRect(
                        color = progressColor,
                        topLeft = topLeft,
                        size = size,
                    )
                }

                startLevel in card.level -> {
                    val nextLevelInfo = card.nextLevelInfo
                    if (nextLevelInfo != null) {
                        val startLevelDotCenterX = levelToDotCenterX[startLevel] ?: 0f
                        val endLevelDotCenterX = levelToDotCenterX[endLevel] ?: 0f
                        val startLevelRequiredPurchaseSum = startLevel.requiredPurchaseSum
                        val nextLevelRemainingPurchaseSum =
                            nextLevelInfo.requiredPurchaseSum - card.totalPurchaseSum
                        // Subtract start level required purchase to count from zero
                        val nextLevelRequiredPurchaseSum =
                            nextLevelInfo.requiredPurchaseSum - startLevelRequiredPurchaseSum
                        val levelProgressFraction = 1f -
                                (nextLevelRemainingPurchaseSum.toFloat() / nextLevelRequiredPurchaseSum)
                        val topLeft = Offset(
                            x = startLevelDotCenterX,
                            y = (size.height - trackWidth) / 2,
                        )
                        val size = Size(
                            width = (endLevelDotCenterX - startLevelDotCenterX) * levelProgressFraction,
                            height = trackSize.height,
                        )
                        drawRect(
                            color = progressColor,
                            topLeft = topLeft,
                            size = size,
                        )
                    }
                }
            }
        }
}

private fun DrawScope.drawDots(
    cardLevel: LoyaltyCardLevel,
    levelToDotCenterX: Map<LoyaltyCardLevel, Float>,
    dotRadius: Float,
    trackColor: Color,
    progressColor: Color,
) {
    levelToDotCenterX.forEach { (level, dotCenterX) ->
        val color = if (level in cardLevel) progressColor else trackColor
        val center = Offset(x = dotCenterX, y = size.height / 2)
        drawCircle(
            color = color,
            radius = dotRadius,
            center = center,
        )
    }
}

@Suppress("MagicNumber")
@Preview
//@FontScalePreviews
//@DensityPreviews
@Composable
private fun PreviewPrime() {
    ZarinaPreview {
        LoyaltyCard(
            card = remember {
                val level = LoyaltyCardLevel.PRIME
                val nextLevel = LoyaltyCardLevel.PRIORITY
                val totalPurchaseSum = 2500
                FakeDataGenerator.getLoyaltyCard(
                    level = level,
                    nextLevelInfo = LoyaltyCard.NextLevelInfo(
                        level = nextLevel,
                        requiredPurchaseSum = 10000,
                    ),
                    totalPurchaseSum = totalPurchaseSum,
                )
            },
            onLevelInfoClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun PreviewPriority() {
    ZarinaPreview {
        LoyaltyCard(
            card = remember {
                val level = LoyaltyCardLevel.PRIORITY
                val nextLevel = LoyaltyCardLevel.STAR
                val totalPurchaseSum = 25000
                FakeDataGenerator.getLoyaltyCard(
                    level = level,
                    nextLevelInfo = LoyaltyCard.NextLevelInfo(
                        level = nextLevel,
                        requiredPurchaseSum = 30000,
                    ),
                    totalPurchaseSum = totalPurchaseSum,
                )
            },
            onLevelInfoClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun PreviewStar() {
    ZarinaPreview {
        LoyaltyCard(
            card = remember {
                val level = LoyaltyCardLevel.STAR
                val totalPurchaseSum = 50000
                FakeDataGenerator.getLoyaltyCard(
                    level = level,
                    totalPurchaseSum = totalPurchaseSum,
                    nextLevelInfo = null,
                )
            },
            onLevelInfoClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun PreviewBackSide() {
    ZarinaPreview {
        LoyaltyCard(
            card = remember {
                val level = LoyaltyCardLevel.STAR
                val totalPurchaseSum = 50000
                FakeDataGenerator.getLoyaltyCard(
                    level = level,
                    totalPurchaseSum = totalPurchaseSum,
                    nextLevelInfo = null,
                )
            },
            initialSide = LoyaltyCardSide.BACK,
            onLevelInfoClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}

enum class LoyaltyCardSide { FRONT, BACK }

private val LoyaltyCardLevel.contentColor: Color
    @Composable
    get() = when (this) {
        LoyaltyCardLevel.PRIME, LoyaltyCardLevel.PRIORITY -> {
            UiKitTheme.colors.text.general.regular.default
        }

        LoyaltyCardLevel.STAR -> UiKitTheme.colors.text.general.inversed.default
    }

private const val ROTATION_FRONT_SIDE = 0f
private const val ROTATION_BACK_SIDE = 180f
private const val ROTATION_TURN_THRESHOLD = ROTATION_BACK_SIDE / 2

private val ShapeDefault: Shape get() = RoundedCornerShape(12.dp)
private val ContentPaddingFrontSide: PaddingValues get() = PaddingValues(30.dp)
private val ContentPaddingBackSide: PaddingValues get() = PaddingValues(16.dp)

private val ProgressBarTrackWidth: Dp get() = 1.dp
private val ProgressBarDotSize: Dp get() = 8.dp

private const val Tag = "LoyaltyCard"
