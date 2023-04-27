package ru.zarina.zarina.ui.screens.product.components.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.DeliveryAvailabilityProvider
import ru.zarina.zarina.ui.screens.product.components.sections.ContentType.*
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DeliveryAvailabilitySection(
    deliveryAvailability: DeliveryAvailability?,
    modifier: Modifier = Modifier,
) {
    val contentType = when {
        deliveryAvailability != null -> Availability(deliveryAvailability)
        else -> Error
    }
    AnimatedContent(
        targetState = contentType,
        modifier = modifier
            .border(
                width = 1.dp,
                color = UiKitTheme.colors.primaryBorderColor
            )
            .padding(24.dp),
        label = "content type"
    ) { type ->
        when (type) {
            is Loading -> {} // TODO
            is Availability -> DeliveryInformation(deliveryAvailability = type.deliveryAvailability)
            is Error -> {} // TODO
        }
    }
}

sealed class ContentType {
    object Loading : ContentType()
    class Availability(val deliveryAvailability: DeliveryAvailability) : ContentType()
    object Error : ContentType()
}

@Composable
private fun DeliveryInformation(
    deliveryAvailability: DeliveryAvailability,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        DeliveryInformationHeader(deliveryAvailability = deliveryAvailability)
    }
}

@Composable
private fun DeliveryInformationHeader(
    deliveryAvailability: DeliveryAvailability,
    modifier: Modifier = Modifier,
) {
    val cityTextColor = UiKitTheme.colors.primaryAccentColor
    val headerString = stringResource(R.string.delivery_to, deliveryAvailability.cityName)
    val headerColored = remember(headerString) {
        buildAnnotatedString {
            append(headerString)
            val cityNameStartIndex = headerString.indexOf(deliveryAvailability.cityName)
            val cityNameEndIndex = cityNameStartIndex + deliveryAvailability.cityName.length
            addStyle(
                style = SpanStyle(color = cityTextColor),
                start = cityNameStartIndex,
                end = cityNameEndIndex
            )
        }
    }
    Text(
        text = headerColored,
        style = UiKitTheme.typography.deliveryInformationHeader,
        color = UiKitTheme.colors.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DeliveryAvailabilitySectionPreview(
    @PreviewParameter(DeliveryAvailabilityProvider::class, limit = 1)
    deliveryAvailability: DeliveryAvailability,
) {
    ZarinaTheme {
        DeliveryAvailabilitySection(
            deliveryAvailability = deliveryAvailability,
        )
    }
}
