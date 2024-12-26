package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicommon.YandexCaptchaEvent
import ru.livetyping.zarina.core.uicomponent.captcha.YandexCaptchaDialog
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.component.PhoneChangingContent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.component.PhoneChangingTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.model.PhoneChangingEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.model.PhoneChangingState

@Composable
internal fun PhoneChangingScreen(
    navActions: PhoneChangingNavActions,
    viewModel: PhoneChangingViewModel = hiltViewModel(),
) {
    val state by viewModel.phoneChangingState.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        onEvent = viewModel::onPhoneChangingEvent,
        onYandexCaptchaEvent = viewModel::onYandexCaptchaEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    state: PhoneChangingState,
    onEvent: (PhoneChangingEvent) -> Unit,
    onYandexCaptchaEvent: (YandexCaptchaEvent) -> Unit,
    sideEffects: Flow<PhoneChangingSideEffect>,
    navActions: PhoneChangingNavActions,
) {
    PhoneChangingScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colors.background.general.regular.default)
                .windowInsetsPadding(
                    WindowInsets.statusBars
                        .union(WindowInsets.displayCutout)
                        .union(WindowInsets.ime),
                )
                .bottomNavBarPadding(WindowInsets.ime),
        ) {
            PhoneChangingTopBar(
                onBackClicked = { onEvent(PhoneChangingEvent.BackClicked) },
            )

            PhoneChangingContent(
                state = state,
                onEvent = onEvent,
            )
        }

        if (state.visibleYandexCaptcha != null) {
            YandexCaptchaDialog(
                captcha = state.visibleYandexCaptcha,
                onEvent = onYandexCaptchaEvent,
            )
        }
    }
}
