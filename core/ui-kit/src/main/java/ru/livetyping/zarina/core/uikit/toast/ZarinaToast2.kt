package ru.livetyping.zarina.core.uikit.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2
import ru.livetyping.zarina.core.uicompose.text.textString
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.product.ProductDefaults
import ru.livetyping.zarina.core.uikit.text.withZarinaBrackets
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.core.uikit.toast.ZarinaToast2Defaults.HorizontalPaddingWithStartImage
import ru.livetyping.zarina.core.uikit.toast.ZarinaToast2Defaults.VerticalPaddingWithStartImage
import ru.livetyping.zarina.core.resource.R as RCommon

// TODO: [Top] Rename after full migration
@Composable
public fun ZarinaToast2(
    message: ZarinaToastMessage2,
    modifier: Modifier = Modifier,
    backgroundColor: Color = UiKitTheme2.colors.mainBlack,
) {
    val minHeight = when (message.size) {
        ZarinaToastMessage2.Size.Medium -> ZarinaToast2Defaults.MinHeightMedium
        ZarinaToastMessage2.Size.Large -> ZarinaToast2Defaults.MinHeightLarge
    }

    CompositionLocalProvider(LocalContentColor provides UiKitTheme2.colors.white) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(ZarinaToast2Defaults.Shape)
                .background(backgroundColor)
                .defaultMinSize(minHeight = minHeight)
                .padding(ZarinaToast2Defaults.paddingFromMessage(message)),
        ) {
            message.startContent?.let {
                val paddingModifier = when (it) {
                    is ZarinaToastMessage2.StartContent.Image -> {
                        Modifier.padding(
                            start = HorizontalPaddingWithStartImage,
                            top = VerticalPaddingWithStartImage,
                            bottom = VerticalPaddingWithStartImage,
                        )
                    }

                    is ZarinaToastMessage2.StartContent.Icon -> Modifier
                }

                StartContent(
                    startContent = it,
                    modifier = paddingModifier,
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            val textAlign = when (message.size) {
                ZarinaToastMessage2.Size.Medium -> null
                ZarinaToastMessage2.Size.Large -> TextAlign.Center
            }

            val text = textString(message.text).let {
                if (message.addBracketsToText) it.withZarinaBrackets() else it
            }.uppercase()

            Text(
                text = text,
                style = UiKitTheme2.typography.body2,
                textAlign = textAlign,
                modifier = Modifier.weight(1f),
            )

            message.endContent?.let {
                Spacer(modifier = Modifier.width(16.dp))

                EndContent(
                    endContent = it,
                    modifier = Modifier.align(Alignment.Top),
                )
            }
        }
    }
}

@Composable
private fun StartContent(
    startContent: ZarinaToastMessage2.StartContent,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (startContent) {
            is ZarinaToastMessage2.StartContent.Icon -> {
                Icon(
                    imageVector = ImageVector.vectorResource(startContent.resId),
                    contentDescription = startContent.contentDescription,
                    modifier = Modifier.size(16.dp),
                )
            }

            is ZarinaToastMessage2.StartContent.Image -> {
                val height = ZarinaToast2Defaults.MinHeightLarge -
                        VerticalPaddingWithStartImage * 2

                AsyncImage(
                    model = startContent.url,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .height(height)
                        .aspectRatio(ProductDefaults.MediaAspectRatio),
                )
            }
        }
    }
}

@Composable
private fun EndContent(
    endContent: ZarinaToastMessage2.EndContent,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (endContent) {
            ZarinaToastMessage2.EndContent.CloseButton -> {
                val controller = LocalZarinaToastController2.current

                ZarinaIconButton(
                    onClick = controller::cancelCurrentToast,
                    indication = ripple(
                        color = UiKitTheme2.colors.white,
                        radius = 16.dp,
                        bounded = false,
                    ),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_cross_24),
                        contentDescription = null, // TODO: [Top] Implement
                        tint = UiKitTheme2.colors.white,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewMedium() {
    ZarinaTheme2 {
        val message = remember {
            ZarinaToastMessage2(
                text = ru.livetyping.zarina.core.text.Text.String("Не удалось добавить товар в избранное"),
                startContent = ZarinaToastMessage2.StartContent.Icon(
                    RCommon.drawable.ic_cross_24,
                    contentDescription = null,
                ),
                endContent = null,
            )
        }

        ZarinaToast2(message)
    }
}

@Composable
@Preview
private fun PreviewLarge() {
    ZarinaTheme2 {
        val message = remember {
            ZarinaToastMessage2(
                text = ru.livetyping.zarina.core.text.Text.String("Товар добавлен в корзину".withZarinaBrackets()),
                startContent = ZarinaToastMessage2.StartContent.Image(""),
                endContent = ZarinaToastMessage2.EndContent.CloseButton,
                size = ZarinaToastMessage2.Size.Large,
            )
        }

        ZarinaToast2(message)
    }
}

// TODO: [Top] Rename after full migration
public object ZarinaToast2Defaults {
    internal val Shape: Shape = RoundedCornerShape(1.dp)

    internal val MinHeightMedium: Dp get() = 56.dp
    internal val MinHeightLarge: Dp get() = 80.dp

    private val HorizontalPaddingDefault: Dp get() = 16.dp
    internal val HorizontalPaddingWithStartImage: Dp get() = 6.dp

    private val VerticalPaddingDefault: Dp get() = 12.dp
    internal val VerticalPaddingWithStartImage: Dp get() = HorizontalPaddingWithStartImage

    @Composable
    internal fun paddingFromMessage(message: ZarinaToastMessage2): PaddingValues {
        val startPadding = when (message.startContent) {
            is ZarinaToastMessage2.StartContent.Icon -> HorizontalPaddingDefault
            is ZarinaToastMessage2.StartContent.Image -> 0.dp
            null -> 16.dp
        }
        val topPadding = when (message.startContent) {
            is ZarinaToastMessage2.StartContent.Icon -> VerticalPaddingDefault
            is ZarinaToastMessage2.StartContent.Image -> 0.dp
            null -> VerticalPaddingDefault
        }
        val endPadding = when (message.endContent) {
            ZarinaToastMessage2.EndContent.CloseButton -> 0.dp
            null -> HorizontalPaddingDefault
        }
        val bottomPadding = when (message.startContent) {
            is ZarinaToastMessage2.StartContent.Icon -> VerticalPaddingDefault
            is ZarinaToastMessage2.StartContent.Image -> 0.dp
            null -> VerticalPaddingDefault
        }
        return PaddingValues(
            start = startPadding,
            top = topPadding,
            end = endPadding,
            bottom = bottomPadding,
        )
    }
}
