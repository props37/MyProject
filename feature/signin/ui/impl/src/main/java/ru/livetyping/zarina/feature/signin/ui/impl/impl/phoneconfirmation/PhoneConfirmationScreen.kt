package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

import androidx.compose.foundation.background
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
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun PhoneConfirmationScreen(
    navActions: PhoneConfirmationNavActions,
    viewModel: PhoneConfirmationViewModel = hiltViewModel(),
) {
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val otpState by viewModel.otpState.collectAsStateWithLifecycle()

    ScreenContent(
        phone = phone,
        otpState = otpState,
        onEvent = viewModel::onPhoneConfirmationEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    phone: PhoneNumber,
    otpState: TextFieldOtpState,
    onEvent: (PhoneConfirmationEvent) -> Unit,
    sideEffects: Flow<PhoneConfirmationSideEffect>,
    navActions: PhoneConfirmationNavActions,
) {
    PhoneConfirmationScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TODO()
        // TODO: [Top] Implement
    }
}
