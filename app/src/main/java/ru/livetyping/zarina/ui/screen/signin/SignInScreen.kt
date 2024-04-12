package ru.livetyping.zarina.ui.screen.signin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.signin.SignInScreenComponents.SignInTypePager
import ru.livetyping.zarina.ui.screen.signin.SignInScreenComponents.SignInTypeTabRow
import ru.livetyping.zarina.ui.screen.signin.SignInScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.signin.SignInViewModel.SideEffect
import ru.livetyping.zarina.ui.screen.signin.SignInViewModel.SignInType
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration
import ru.livetyping.zarina.util.compose.tryRequestFocus

@Composable
fun SignInScreen(
    navigate: (SignInScreenAction) -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val signInTypes by viewModel.signInTypes.collectAsStateWithLifecycle()
    val currentSignInType by viewModel.currentSignInType.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val isSignInButtonLoading by viewModel.isSignInButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        signInTypes = signInTypes,
        currentSignInType = currentSignInType,
        onSignInTypeChanged = viewModel::onSignInTypeChanged,
        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        password = password,
        onPasswordChanged = viewModel::onPasswordChanged,
        phone = phone,
        onPhoneChanged = viewModel::onPhoneChanged,
        onSignInClicked = viewModel::onSignInClicked,
        isSignInButtonLoading = isSignInButtonLoading,
        onForgotPasswordClicked = viewModel::onForgotPasswordClicked,
        onSignUpClicked = viewModel::onSignUpClicked,
        onUrlClicked = viewModel::onUrlClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    signInTypes: ImmutableList<SignInType>,
    currentSignInType: SignInType,
    onSignInTypeChanged: (SignInType) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    onSignInClicked: () -> Unit,
    isSignInButtonLoading: Boolean,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (SignInScreenAction) -> Unit,
) {
    val updatedKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)
    val emailFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        emailFocusRequester.tryRequestFocus()
    }

    SignInScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

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

        LaunchedEffect(signInTypePagerState) {
            snapshotFlow { signInTypePagerState.currentPage }.collect {
                updatedKeyboardController?.hide()
            }
        }

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
            email = email,
            onEmailChanged = onEmailChanged,
            password = password,
            onPasswordChanged = onPasswordChanged,
            phone = phone,
            onPhoneChanged = onPhoneChanged,
            onSignInClicked = onSignInClicked,
            isSignInButtonLoading = isSignInButtonLoading,
            onForgotPasswordClicked = onForgotPasswordClicked,
            onSignUpClicked = onSignUpClicked,
            onUrlClicked = onUrlClicked,
            emailFocusRequester = emailFocusRequester,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            signInTypes = remember { SignInType.entries.toImmutableList() },
            currentSignInType = SignInType.EMAIL,
            onSignInTypeChanged = {},
            email = "",
            onEmailChanged = {},
            password = "",
            onPasswordChanged = {},
            phone = "+7",
            onPhoneChanged = {},
            onSignInClicked = {},
            isSignInButtonLoading = false,
            onForgotPasswordClicked = {},
            onSignUpClicked = {},
            onUrlClicked = {},
            onBackClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun PreviewPhone() {
    ZarinaPreview {
        ScreenContent(
            signInTypes = remember { SignInType.entries.toImmutableList() },
            currentSignInType = SignInType.PHONE,
            onSignInTypeChanged = {},
            email = "",
            onEmailChanged = {},
            password = "",
            onPasswordChanged = {},
            phone = "+7",
            onPhoneChanged = {},
            onSignInClicked = {},
            isSignInButtonLoading = false,
            onForgotPasswordClicked = {},
            onSignUpClicked = {},
            onUrlClicked = {},
            onBackClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
