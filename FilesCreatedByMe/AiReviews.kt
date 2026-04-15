// ========== REVIEW FROM HERE ==========
package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.AiReviewsState.Tag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.AiReviewsState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun AiReviews(
    aiReviewsState: AiReviewsState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_ai_24),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(13.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = stringResource(R.string.product_ai_reviews_neural_highlight).uppercase(),
                    style = UiKitTheme2.typography.body2,
                    color = UiKitTheme2.colors.mainBlack,
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = stringResource(R.string.product_ai_reviews_open_source_reviews).uppercase(),
                    style = UiKitTheme2.typography.body2,
                    color = UiKitTheme2.colors.middleGray,
                )
            }


        }

        if (aiReviewsState.tags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(15.dp))

            TagsSection(
                tags = aiReviewsState.tags,
            )
        }

        if (aiReviewsState.longDescription != null) {
            Spacer(modifier = Modifier.height(15.dp))

            LongDescriptionSection(
                longDescription = aiReviewsState.longDescription,
            )
        }
    }
}

@Composable
private fun TagsSection(
    tags:  List<Tag>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier,
    ) {
        tags.forEach {
            TagItem(tag = it)
        }
    }
}

@Composable
private fun TagItem(
    tag: Tag,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = UiKitTheme2.colors.lightGray,
                shape = RoundedCornerShape(
                    topStart = CornerSize(50),
                    topEnd = CornerSize(0),
                    bottomEnd = CornerSize(50),
                    bottomStart = CornerSize(50),
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = "«${tag.text}»".uppercase(),
            style = UiKitTheme2.typography.body2,
            color = UiKitTheme2.colors.mainBlack,
        )
    }
}

@Composable
private fun LongDescriptionSection(
    longDescription: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = longDescription.uppercase(),
        style = UiKitTheme2.typography.body2,
        color = UiKitTheme2.colors.mainBlack,
        modifier = modifier,
    )
}

@Composable
@Preview
private fun Preview(modifier: Modifier = Modifier) {
    ZarinaTheme2 {
        val aiReviewsState = AiReviewsState(
            longDescription = "не ТОНКАЯ ЮБКА С ИНТЕРЕСНОЙ РАСЦВЕТКОЙ И ПЛОТНОЙ ПОСАДКОЙ, УКРАШЕНА ЖЕСТКОЙ РЕЗИНКОЙ ДЛЯ КОМФОРТНОЙ ФИКСАЦИИ НА ТАЛИИ",
            tags = listOf(
                Tag("теплая", ProductAiReviews.Tag.Focus.POSITIVE),
                Tag("КАЧЕСТВЕННАЯ ФУРНИТУРА", ProductAiReviews.Tag.Focus.POSITIVE),
                Tag("ЛЕГКАЯ", ProductAiReviews.Tag.Focus.POSITIVE),
                Tag("АККУРАТНАЯ", ProductAiReviews.Tag.Focus.POSITIVE),
                Tag("УДОБНАЯ ПОСАДКА", ProductAiReviews.Tag.Focus.POSITIVE),
            )
        )

        AiReviews(
            aiReviewsState = aiReviewsState,
            modifier = modifier
                .background(Color.White)
                .padding(16.dp)
        )
    }
}
// ========== TO HERE ==========