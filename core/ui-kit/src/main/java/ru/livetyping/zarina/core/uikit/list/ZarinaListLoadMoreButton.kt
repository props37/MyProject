package ru.livetyping.zarina.core.uikit.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uikit.text.withZarinaBrackets
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
public fun ZarinaListLoadMoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        TextButton(
            onClick = onClick,
            shape = RoundedCornerShape(1.dp),
            modifier = Modifier.padding(vertical = 32.dp),
        ) {
            Text(
                text = stringResource(R.string.res_load_more).withZarinaBrackets().uppercase(),
                style = UiKitTheme2.typography.body,
                color = UiKitTheme2.colors.mainBlack,
            )
        }
    }
}
