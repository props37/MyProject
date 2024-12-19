package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.signup.ui.impl.R

@Composable
internal fun SignUpTopBar(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = onBackClicked,
                iconSize = 20.dp,
                modifier = Modifier.padding(start = 2.dp),
            )
        },
        centerContent = {
            Text(
                text = stringResource(R.string.sign_up_registration),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
