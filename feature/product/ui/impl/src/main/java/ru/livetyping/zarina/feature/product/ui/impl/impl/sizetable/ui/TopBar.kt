package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.product.ui.impl.R

@Composable
internal fun TopBar(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            Text(
                text = stringResource(R.string.product_how_to_chose_size_question).uppercase(),
                style = UiKitTheme2.typography.h3,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        endContent = {
            ZarinaCloseIconButton(onClick = onCloseClicked)
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
