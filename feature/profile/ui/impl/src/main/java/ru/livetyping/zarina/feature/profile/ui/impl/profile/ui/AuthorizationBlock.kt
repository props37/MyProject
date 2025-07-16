package ru.livetyping.zarina.feature.profile.ui.impl.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.profile.ui.impl.R

@Composable
internal fun AuthorizationBlock(
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.profile_sign_in_or_sign_up).uppercase(),
            style = UiKitTheme2.typography.bodyBold,
            color = UiKitTheme2.colors.mainBlack,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.profile_authorization_description).uppercase(),
            style = UiKitTheme2.typography.body,
            color = UiKitTheme2.colors.mainBlack,
        )

        Spacer(modifier = Modifier.height(28.dp))

        ZarinaButton(
            onClick = onSignInClicked,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.profile_sign_in).uppercase())
        }
        Spacer(modifier = Modifier.height(8.dp))
        ZarinaButton(
            onClick = onSignUpClicked,
            colors = ZarinaButtonDefaults.outlinedColors(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.profile_sign_up).uppercase())
        }
    }
}
