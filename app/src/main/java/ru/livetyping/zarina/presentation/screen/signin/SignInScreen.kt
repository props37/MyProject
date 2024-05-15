package ru.livetyping.zarina.presentation.screen.signin

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.signin.SignInScreenComponents.SignInTypePager
import ru.livetyping.zarina.presentation.screen.signin.SignInScreenComponents.SignInTypeTabRow
import ru.livetyping.zarina.presentation.screen.signin.SignInScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.signin.SignInViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.signin.SignInViewModel.SignInType
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration

// TODO: [High] Request TextField focus automatically

@Composable
fun SignInScreen(
    navigate: (SignInScreenAction) -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val signInTypes by viewModel.signInTypes.collectAsStateWithLifecycle()
    val currentSignInType by viewModel.currentSignInType.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val isEmailInvalid by viewModel.isEmailInvalid.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val isPasswordInvalid by viewModel.isPasswordInvalid.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val isPhoneInvalid by viewModel.isPhoneInvalid.collectAsStateWithLifecycle()
    val isSignInButtonLoading by viewModel.isSignInButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        signInTypes = signInTypes,
        currentSignInType = currentSignInType,
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
    isEmailInvalid: Boolean,
    password: String,
    onPasswordChanged: (String) -> Unit,
    isPasswordInvalid: Boolean,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    isPhoneInvalid: Boolean,
    onSignInClicked: () -> Unit,
    isSignInButtonLoading: Boolean,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (SignInScreenAction) -> Unit,
) {
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
            isEmailInvalid = isEmailInvalid,
            password = password,
            onPasswordChanged = onPasswordChanged,
            isPasswordInvalid = isPasswordInvalid,
            phone = phone,
            onPhoneChanged = onPhoneChanged,
            isPhoneInvalid = isPhoneInvalid,
            onSignInClicked = onSignInClicked,
            isSignInButtonLoading = isSignInButtonLoading,
            onForgotPasswordClicked = onForgotPasswordClicked,
            onSignUpClicked = onSignUpClicked,
            onUrlClicked = onUrlClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            signInTypes = remember { SignInType.entries.toImmutableList() },
            currentSignInType = SignInType.EMAIL,
            onSignInTypeChanged = {},
            email = "",
            onEmailChanged = {},
            isEmailInvalid = false,
            password = "",
            onPasswordChanged = {},
            isPasswordInvalid = false,
            phone = "+7",
            onPhoneChanged = {},
            isPhoneInvalid = false,
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
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun PreviewPhone() {
    ZarinaPreview {
        ScreenContent(
            signInTypes = remember { SignInType.entries.toImmutableList() },
            currentSignInType = SignInType.PHONE,
            onSignInTypeChanged = {},
            email = "",
            onEmailChanged = {},
            isEmailInvalid = false,
            password = "",
            onPasswordChanged = {},
            isPasswordInvalid = false,
            phone = "+7",
            onPhoneChanged = {},
            isPhoneInvalid = false,
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
