package ru.livetyping.zarina.ui.screen.signin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.ui.common.component.tab.ZarinaTab
import ru.livetyping.zarina.ui.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaPasswordTextField
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaPhoneNumberTextField
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.signin.SignInScreenComponents.SignInBlock
import ru.livetyping.zarina.ui.screen.signin.SignInScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.signin.SignInViewModel.SideEffect
import ru.livetyping.zarina.ui.screen.signin.SignInViewModel.SignInType
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration
import ru.livetyping.zarina.util.compose.tryRequestFocus

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val signInTypes by viewModel.signInTypes.collectAsStateWithLifecycle()
    val currentSignInType by viewModel.currentSignInType.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle()

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
        onSignUpClicked = viewModel::onSignUpClicked,
        onUrlClicked = viewModel::onUrlClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
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
    onSignUpClicked: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
) {
    SignInScreenBehavior(sideEffects = sideEffects)

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

        val signInTypePagerState = rememberPagerState { signInTypes.size }

        PagerTabRowIntegration(
            pagerState = signInTypePagerState,
            tabs = signInTypes,
            currentTab = currentSignInType,
            onCurrentTabChanged = onSignInTypeChanged,
        )

        // TODO: [High] Extract
        ZarinaTabRow(
            selectedTabIndex = signInTypePagerState.currentPage,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            signInTypes.forEach { type ->
                val textResId = when (type) {
                    SignInType.EMAIL -> R.string.by_email
                    SignInType.PHONE -> R.string.by_phone
                }

                ZarinaTab(
                    text = stringResource(textResId),
                    onClick = { onSignInTypeChanged(type) },
                    isSelected = type == currentSignInType,
                )
            }
        }

        val updatedKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)
        LaunchedEffect(signInTypePagerState) {
            snapshotFlow { signInTypePagerState.currentPage }.collect {
                updatedKeyboardController?.hide()
            }
        }

        // TODO: [High] Extract
        val emailFocusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            emailFocusRequester.tryRequestFocus()
        }

        // TODO: [High] Extract
        HorizontalPager(
            state = signInTypePagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val signInType = signInTypes[page]
            when (signInType) {
                SignInType.EMAIL -> {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Spacer(modifier = Modifier.height(SignInScreenComponents.TopPadding))

                        ZarinaTextField(
                            value = email,
                            onValueChanged = onEmailChanged,
                            label = { Text(text = stringResource(R.string.email)) },
                            placeholder = {
                                Text(text = stringResource(R.string.email_text_field_placeholder))
                            },
                            innerTrailingContent = {
                                ZarinaTextFieldDefaults.ClearButton(
                                    isVisible = email.isNotEmpty(),
                                    onClick = { onEmailChanged("") },
                                )
                            },
                            keyboardOptions = remember {
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next,
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .focusRequester(emailFocusRequester),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ZarinaPasswordTextField(
                            password = password,
                            onPasswordChanged = onPasswordChanged,
                            keyboardOptions = remember {
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done,
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )

                        ZarinaButton(
                            onClick = { /*TODO*/ },
                            size = ZarinaButtonSize.Medium,
                            colors = ZarinaButtonDefaults.backlessColors(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            indication = null,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.forgot_password_question).uppercase(),
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        SignInBlock(
                            onSignInClicked = onSignInClicked,
                            onSignUpClicked = onSignUpClicked,
                            onUrlClicked = onUrlClicked,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }

                SignInType.PHONE -> {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Spacer(modifier = Modifier.height(32.dp))

                        ZarinaPhoneNumberTextField(
                            phoneNumber = phone,
                            onPhoneNumberChanged = onPhoneChanged,
                            keyboardOptions = remember {
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Done,
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        SignInBlock(
                            onSignInClicked = onSignInClicked,
                            onSignUpClicked = onSignUpClicked,
                            onUrlClicked = onUrlClicked,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
