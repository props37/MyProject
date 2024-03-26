package ru.zarina.zarina.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme

object ProfileScreenComponents {

    @Composable
    fun TopBar(
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.profile),
                    style = UiKitTheme.typography.primary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            },
            modifier = modifier,
        )
    }

    @Composable
    fun AuthorizationSuggestion(
        onSignInClicked: () -> Unit,
        onSignUpClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Text(
                text = stringResource(R.string.sign_in_or_sign_up),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.profile_authorization_description),
                style = UiKitTheme.typography.tertiary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
            )

            Spacer(modifier = Modifier.height(28.dp))

            ZarinaButton(
                onClick = onSignInClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.sign_in).uppercase())
            }
            Spacer(modifier = Modifier.height(8.dp))
            ZarinaButton(
                onClick = onSignUpClicked,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.sign_up).uppercase())
            }
        }
    }

    @Composable
    fun AppVersion(
        version: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            text = stringResource(R.string.app_version_s, version),
            style = UiKitTheme.typography.footnote.regular,
            color = UiKitTheme.colors.text.general.regular.muted,
            modifier = modifier,
        )
    }
}
