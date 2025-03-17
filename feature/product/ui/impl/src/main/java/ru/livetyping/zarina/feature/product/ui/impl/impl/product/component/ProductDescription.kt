package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
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
    modelInfo: ProductDetailed.ModelInfo?,
    modifier: Modifier = Modifier,
) {
    ZarinaExpandableItem(
        header = {
            Text(text = stringResource(R.string.product_details))
        },
        modifier = modifier,
    ) {
        Column {
            Description(description)

            if (modelInfo != null && !modelInfo.isEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                ModelInfo(modelInfo)
            }
        }
    }
}

@Composable
internal fun Description(
    description: List<ProductDetailed.DescriptionEntry>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = DescriptionArrangement,
        modifier = modifier,
    ) {
        description.forEach { descriptionEntry ->
            val title = descriptionEntry.title
            val body = descriptionEntry.body
            key(title + body) {
                val text = rememberProductDescriptionText(title, body)
                Text(text = text)
            }
        }
    }
}

@Composable
internal fun ModelInfo(
    modelInfo: ProductDetailed.ModelInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = DescriptionArrangement,
        modifier = modifier,
    ) {
        modelInfo.productSize?.let { productSize ->
            val title = stringResource(R.string.product_size_on_model)
            val text = rememberProductDescriptionText(title, productSize)
            Text(text = text)
        }

        modelInfo.modelParams?.let { modelParams ->
            val title = stringResource(R.string.product_model_parameters)
            val text = rememberProductDescriptionText(title, modelParams)
            Text(text = text)
        }
    }
}

@Composable
private fun rememberProductDescriptionText(
    title: String,
    body: String,
): AnnotatedString {
    val titleStyle = UiKitTheme.typography.tertiary.regular
    val bodyStyle = UiKitTheme.typography.tertiary.light
    return remember(title, body, titleStyle, bodyStyle) {
        buildAnnotatedString {
            withStyle(titleStyle.toSpanStyle()) {
                append(title)
                append(Colon)
            }
            withStyle(bodyStyle.toSpanStyle()) {
                append(Space)
                append(body)
            }
        }
    }
}

private val DescriptionArrangement: Arrangement.Vertical
    get() = Arrangement.spacedBy(2.dp)

private const val Colon = ':'
private const val Space = ' '
