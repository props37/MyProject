package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.AiReviewsState.Tag

@Composable
internal fun AiReviews(
    longDescription : String?,
    tags : List<Tag>,
    //tags: ImmutableList<Tag>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ru.livetyping.zarina.core.resource.R.drawable.ic_ai_24),
                contentDescription = null,
                modifier = Modifier
                    .size(13.dp)
            )

            Text(
                text = stringResource(R.string.product_ai_reviews_neural_highlight).uppercase(),
                style = UiKitTheme2.typography.body,
                color = UiKitTheme2.colors.mainBlack,
                modifier = Modifier
                    .padding(start = 25.dp)
            )
        }

    }
}