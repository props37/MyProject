package ru.livetyping.zarina.feature.product.ui.impl.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.uikit.item.ZarinaExpandableItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.product.ui.impl.R

@Composable
internal fun ProductDescription(
    description: List<ProductDetailed.DescriptionEntry>,
    modifier: Modifier = Modifier,
) {
    ZarinaExpandableItem(
        header = {
            Text(text = stringResource(R.string.product_details))
        },
        modifier = modifier,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            description.forEach { descriptionEntry ->
                val text = rememberProductDescriptionEntryText(descriptionEntry)
                Text(text = text)
            }
        }
    }
}

@Composable
private fun rememberProductDescriptionEntryText(
    descriptionEntry: ProductDetailed.DescriptionEntry,
): AnnotatedString {
    val titleStyle = UiKitTheme.typography.tertiary.regular
    val bodyStyle = UiKitTheme.typography.tertiary.light
    return remember(descriptionEntry, titleStyle, bodyStyle) {
        buildAnnotatedString {
            withStyle(titleStyle.toSpanStyle()) {
                append(descriptionEntry.title)
                append(Colon)
            }
            withStyle(bodyStyle.toSpanStyle()) {
                append(Space)
                append(descriptionEntry.body)
            }
        }
    }
}

private const val Colon = ':'
private const val Space = ' '
