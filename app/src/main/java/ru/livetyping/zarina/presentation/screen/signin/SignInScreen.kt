package ru.livetyping.zarina.presentation.screen.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.yandexcaptcha.YandexCaptchaDialog
import ru.livetyping.zarina.presentation.common.yandexcaptcha.YandexCaptchaDialogState
import ru.livetyping.zarina.presentation.screen.signin.SignInScreenComponents.SignInTypePager
import ru.livetyping.zarina.presentation.screen.signin.SignInScreenComponents.SignInTypeTabRow
import ru.livetyping.zarina.presentation.screen.signin.SignInScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.signin.SignInViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.signin.SignInViewModel.SignInType
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration

@Composable
fun SignInScreen(
    navigate: (SignInScreenAction) -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val signInTypes by viewModel.signInTypes.collectAsStateWithLifecycle()
    val currentSignInType by viewModel.currentSignInType.collectAsStateWithLifecycle()
    val signInByEmailStep by viewModel.signInByEmailStep.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate,
    )
    val isEmailInvalid by viewModel.isEmailInvalid.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate,
    )
    val isPasswordInvalid by viewModel.isPasswordInvalid.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate,
    )
    val isPhoneInvalid by viewModel.isPhoneInvalid.collectAsStateWithLifecycle()
    val phoneToConfirm by viewModel.phoneToConfirm.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate,
    )
    val isPhoneToConfirmInvalid by viewModel.isPhoneToConfirmInvalid.collectAsStateWithLifecycle()
    val isGetPhoneConfirmationCodeButtonLoading by viewModel.isGetPhoneConfirmationCodeButtonLoading.collectAsStateWithLifecycle()
    val isSignInButtonLoading by viewModel.isSignInButtonLoading.collectAsStateWithLifecycle()
    val yandexCaptchaDialogState by viewModel.yandexCaptchaState.collectAsStateWithLifecycle()

    ScreenContent(
        signInTypes = signInTypes,
        currentSignInType = currentSignInType,
        signInByEmailStep = signInByEmailStep,
        onSignInTypeChanged = viewModel::onSignInTypeChanged,
        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        isEmailInvalid = isEmailInvalid,
        password = password,
        onPasswordChanged = viewModel::onPasswordChanged,
        isPasswordInvalid = isPasswordInvalid,
        phone = phone,
        onPhoneChanged = viewModel::onPhoneChanged,
        isPhoneInvalid = isPhoneInvalid,
        onSignInClicked = viewModel::onSignInClicked,
        isSignInButtonLoading = isSignInButtonLoading,
        phoneToConfirm = phoneToConfirm,
        onPhoneToConfirmChanged = viewModel::onPhoneToConfirmChanged,
        isPhoneToConfirmInvalid = isPhoneToConfirmInvalid,
        onGetPhoneConfirmationCodeClicked = viewModel::onGetPhoneConfirmationCodeClicked,
        isGetPhoneConfirmationCodeButtonLoading = isGetPhoneConfirmationCodeButtonLoading,
        onForgotPasswordClicked = viewModel::onForgotPasswordClicked,
        onSignUpClicked = viewModel::onSignUpClicked,
        onUrlClicked = viewModel::onUrlClicked,
        onBackClicked = viewModel::onBackClicked,
        onScreenOpened = viewModel::onScreenOpened,
        yandexCaptchaDialogState = yandexCaptchaDialogState,
        onYandexCaptchaDismissRequested = viewModel::onYandexCaptchaDismissRequested,
        onYandexCaptchaTokenReceived = viewModel::onYandexCaptchaTokenReceived,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    signInTypes: ImmutableList<SignInType>,
    currentSignInType: SignInType,
    signInByEmailStep: SignInViewModel.SignInByEmailStep,
    onSignInTypeChanged: (SignInType) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    isEmailInvalid: Boolean,
    password: String,
    onPasswordChanged: (String) -> Unit,
    isPasswordInvalid: Boolean,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    isPhoneInvalid: Boolean,
    onSignInClicked: () -> Unit,
    isSignInButtonLoading: Boolean,
    phoneToConfirm: String,
    onPhoneToConfirmChanged: (String) -> Unit,
    isPhoneToConfirmInvalid: Boolean,
    onGetPhoneConfirmationCodeClicked: () -> Unit,
    isGetPhoneConfirmationCodeButtonLoading: Boolean,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    onBackClicked: () -> Unit,
    onScreenOpened: () -> Unit,
    yandexCaptchaDialogState: YandexCaptchaDialogState,
    onYandexCaptchaDismissRequested: () -> Unit,
    onYandexCaptchaTokenReceived: (YandexCaptchaToken) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (SignInScreenAction) -> Unit,
) {
    SignInScreenBehavior(
        onScreenOpened = onScreenOpened,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colors.background.general.regular.default)
                .windowInsetsPadding(
                    WindowInsets.statusBars
                        .union(WindowInsets.displayCutout),
                ),
        ) {
            TopBar(onBackClicked = onBackClicked)

            val signInTypePagerState = rememberPagerState(
                initialPage = signInTypes.indexOf(currentSignInType),
                pageCount = { signInTypes.size },
            )

            PagerTabRowIntegration(
                pagerState = signInTypePagerState,
                tabs = signInTypes,
                currentTab = currentSignInType,
                onCurrentTabChanged = onSignInTypeChanged,
            )

            SignInTypeTabRow(
                signInTypes = signInTypes,
                currentSignInType = currentSignInType,
                onSignInTypeChanged = onSignInTypeChanged,
                signInTypePagerState = signInTypePagerState,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            SignInTypePager(
                signInTypes = signInTypes,
                signInTypePagerState = signInTypePagerState,
                signInByEmailStep = signInByEmailStep,
                email = email,
                onEmailChanged = onEmailChanged,
                isEmailInvalid = isEmailInvalid,
                password = password,
                onPasswordChanged = onPasswordChanged,
                isPasswordInvalid = isPasswordInvalid,
                phone = phone,
                onPhoneChanged = onPhoneChanged,
                isPhoneInvalid = isPhoneInvalid,
                onSignInClicked = onSignInClicked,
                isSignInButtonLoading = isSignInButtonLoading,
                phoneToConfirm = phoneToConfirm,
                onPhoneToConfirmChanged = onPhoneToConfirmChanged,
                isPhoneToConfirmInvalid = isPhoneToConfirmInvalid,
                onGetPhoneConfirmationCodeClicked = onGetPhoneConfirmationCodeClicked,
                isGetPhoneConfirmationCodeButtonLoading = isGetPhoneConfirmationCodeButtonLoading,
                onForgotPasswordClicked = onForgotPasswordClicked,
                onSignUpClicked = onSignUpClicked,
                onUrlClicked = onUrlClicked,
                modifier = Modifier.fillMaxSize(),
            )
        }

        YandexCaptchaDialog(
            state = yandexCaptchaDialogState,
            onDismissRequest = onYandexCaptchaDismissRequested,
            onTokenReceived = onYandexCaptchaTokenReceived,
        )
    }
}
