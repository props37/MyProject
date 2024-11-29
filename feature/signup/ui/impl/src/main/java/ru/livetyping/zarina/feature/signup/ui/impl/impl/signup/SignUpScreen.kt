package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.uicommon.YandexCaptchaEvent
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaDialog
import ru.livetyping.zarina.core.uikit.date.ZarinaDatePickerDialog
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.component.SignUpScreenContent
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.component.SignUpTopBar
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpEvent
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpState
import java.time.LocalDate

@Composable
internal fun SignUpScreen(
    navActions: SignUpNavActions,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()

    ScreenContent(
        signUpState = signUpState,
        onSignUpEvent = viewModel::onSignUpEvent,
        onYandexCaptchaEvent = viewModel::onYandexCaptchaEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenContent(
    signUpState: SignUpState,
    onSignUpEvent: (SignUpEvent) -> Unit,
    onYandexCaptchaEvent: (YandexCaptchaEvent) -> Unit,
    sideEffects: Flow<SignUpSideEffect>,
    navActions: SignUpNavActions,
) {
    SignUpScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    var isDatePickerVisible by remember { mutableStateOf(false) }
    if (isDatePickerVisible) {
        val currentMillis = remember { System.currentTimeMillis() }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentMillis,
            yearRange = remember { User.BIRTH_DATE_MIN_VALUE.year..LocalDate.now().year },
        )

        ZarinaDatePickerDialog(
            onDismissRequest = { isDatePickerVisible = false },
            datePickerState = datePickerState,
            onDateSelected = {
                val event = SignUpEvent.BirthDateEpochMillisChanged(
                    datePickerState.selectedDateMillis,
                )
                onSignUpEvent(event)
                isDatePickerVisible = false
            },
        )
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colors.background.general.regular.default)
                .windowInsetsPadding(
                    WindowInsets.statusBars
                        .union(WindowInsets.displayCutout),
                )
                .bottomNavBarPadding(),
        ) {
            SignUpTopBar(
                onBackClicked = { onSignUpEvent(SignUpEvent.BackClicked) },
            )

            SignUpScreenContent(
                signUpState = signUpState,
                onSignUpEvent = onSignUpEvent,
                onBirthDateClicked = {
                    // TODO: [Top] Implement
                },
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (signUpState.visibleYandexCaptcha != null) {
            YandexCaptchaDialog(
                captcha = signUpState.visibleYandexCaptcha,
                onEvent = onYandexCaptchaEvent,
            )
        }
    }
}
