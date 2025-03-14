package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechange.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaPolicies
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaPhoneTextField
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechange.model.PhoneChangeEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechange.model.PhoneChangeState

@Composable
internal fun PhoneChangeContent(
    state: PhoneChangeState,
    onEvent: (PhoneChangeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.profile_enter_new_phone_number),
            style = UiKitTheme.typography.secondary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.profile_we_will_send_code_for_changing_phone_number),
            style = UiKitTheme.typography.tertiary.light,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(focusRequester) {
            withFrameMillis {}
            focusRequester.tryRequestFocus()
        }

        ZarinaPhoneTextField(
            state = state.phoneTextFieldState,
            isError = state.isPhoneInvalid,
            onKeyboardAction = { defaultAction ->
                defaultAction()
                onEvent(PhoneChangeEvent.RequestPhoneChangeClicked)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(focusRequester)
                .semantics { contentType = ContentType.PhoneNumber },
        )

        Spacer(modifier = Modifier.height(32.dp))

        ZarinaButton(
            onClick = { onEvent(PhoneChangeEvent.RequestPhoneChangeClicked) },
            isLoading = state.isRequestPhoneChangeButtonLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(text = stringResource(ru.livetyping.zarina.core.resource.R.string.res_continue).uppercase())
        }

        Spacer(modifier = Modifier.height(16.dp))

        YandexCaptchaPolicies(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
}
