package ru.zarina.zarina.ui.screens.product.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.CollapsibleContainer
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun DetailsSection(
    description: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    CollapsibleContainer(
        header = {
            Text(
                text = stringResource(id = R.string.details),
                style = UiKitTheme.typography.productDetailsHeader,
                color = UiKitTheme.colors.primaryContentColor,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        },
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            description.forEach {
                Text(
                    text = stringResource(R.string.key_value, it.first, it.second),
                    style = UiKitTheme.typography.productDetailsContent,
                    color = UiKitTheme.colors.primaryContentColor,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}
