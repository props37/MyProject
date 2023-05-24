package ru.zarina.zarina.ui.screens.subscribe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.base.textString
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.form.Input
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.android.openBrowser
import ru.zarina.zarina.utils.compose.navigationOrIme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeScreenContent(
    name: String,
    nameError: Text?,
    onNameChange: (String) -> Unit,
    onNameFocusChange: (Boolean) -> Unit,
    email: String,
    emailError: Text?,
    onEmailChange: (String) -> Unit,
    onEmailFocusChange: (Boolean) -> Unit,
    isSwitchChecked: Boolean,
    onSwitchCheckedChange: (Boolean) -> Unit,
    onLinkClick: (SubscribeViewModel.Link) -> Unit,
    isSubscribeButtonEnabled: Boolean,
    onSubscribeClick: () -> Unit,
    isInputEnabled: Boolean,
    isLoaderVisible: Boolean,
    onBackClick: () -> Unit,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(R.string.subscription_to_product),
                startIcon = {
                    BackButton(onClick = onBackClick)
                }
            )
        },
        isModalLoaderVisible = isLoaderVisible
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(R.string.leave_your_contacts),
                style = UiKitTheme.typography.circle1718,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Input(
                value = name,
                onValueChange = onNameChange,
                hint = stringResource(R.string.name),
                isError = nameError != null,
                error = nameError?.let { textString(it) },
                isEnabled = isInputEnabled,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .onFocusChanged { onNameFocusChange(it.hasFocus) }
                    .fillMaxWidth(),
            )
            Input(
                value = email,
                onValueChange = onEmailChange,
                hint = stringResource(R.string.email),
                isError = emailError != null,
                error = emailError?.let { textString(it) },
                isEnabled = isInputEnabled,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .onFocusChanged { onEmailFocusChange(it.hasFocus) }
                    .fillMaxWidth(),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val text = acknowledgementText()
                ClickableText(
                    text = text,
                    style = UiKitTheme.typography.circle1718.copy(color = UiKitTheme.colors.primaryContentColor),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                ) { index ->
                    val item = text
                        .getStringAnnotations(ANNOTATION_TAG_URL, index, index)
                        .firstOrNull()
                        ?.item
                    val link = item?.let { SubscribeViewModel.Link.valueOf(it) }
                    if (link != null) onLinkClick(link)
                }
                Switch(
                    checked = isSwitchChecked,
                    onCheckedChange = onSwitchCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = UiKitTheme.colors.primaryContentColor,
                        uncheckedThumbColor = UiKitTheme.colors.primaryContentColor
                            .copy(0.08f)
                            .compositeOver(Color.White),
                        checkedTrackColor = UiKitTheme.colors.hint,
                        uncheckedTrackColor = UiKitTheme.colors.hint,
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(12.dp))
            ZarinaTextButton(
                text = stringResource(R.string.subscribe),
                isEnabled = isSubscribeButtonEnabled,
                onClick = onSubscribeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationOrIme.asPaddingValues())
                    .padding(bottom = 24.dp),
            )
        }
    }
}

@Composable
private fun acknowledgementText() = buildAnnotatedString {
    val policy = stringResource(R.string.acknowledgement_subscription_policy)
    val rules = stringResource(R.string.acknowledgement_subscription_rules)
    val data = stringResource(R.string.acknowledgement_subscription_data)
    val fullString = stringResource(
        R.string.acknowledgement_subscription_template,
        policy,
        rules,
        data,
    )
    append(fullString)

    val linkStyle = SpanStyle(color = UiKitTheme.colors.primaryAccentColor)
    ApplyForString(
        fullString = fullString,
        target = policy,
        style = linkStyle,
        annotation = SubscribeViewModel.Link.POLICY.name
    )
    ApplyForString(
        fullString = fullString,
        target = rules,
        style = linkStyle,
        annotation = SubscribeViewModel.Link.RULES.name
    )
    ApplyForString(
        fullString = fullString,
        target = data,
        style = linkStyle,
        annotation = SubscribeViewModel.Link.DATA.name
    )
}

@Composable
private fun AnnotatedString.Builder.ApplyForString(
    fullString: String,
    target: String,
    style: SpanStyle,
    annotation: String,
) {
    val startIndex = fullString.indexOf(target)
    val endIndex = startIndex + target.length
    addStyle(style, startIndex, endIndex)
    addStringAnnotation(ANNOTATION_TAG_URL, annotation, startIndex, endIndex)
}

@Composable
fun SubscribeScreen(
    goBack: () -> Unit,
) {
    val viewModel = hiltViewModel<SubscribeViewModel>()

    val name by viewModel.name.collectAsStateWithLifecycle()
    val nameError by viewModel.nameError.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val emailError by viewModel.emailError.collectAsStateWithLifecycle()
    val isSwitchChecked by viewModel.isSwitchChecked.collectAsStateWithLifecycle()
    val isSubscribeButtonEnabled by viewModel.isSubscribeButtonEnabled.collectAsStateWithLifecycle()
    val isInputEnabled by viewModel.isInputEnabled.collectAsStateWithLifecycle()
    val isLoaderVisible by viewModel.isLoaderVisible.collectAsStateWithLifecycle()

    SubscribeScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SubscribeScreenContent(
        name = name,
        nameError = nameError,
        onNameChange = viewModel::onNameChange,
        onNameFocusChange = viewModel::onNameFocusChange,
        email = email,
        emailError = emailError,
        onEmailChange = viewModel::onEmailChange,
        onEmailFocusChange = viewModel::onEmailFocusChange,
        isSwitchChecked = isSwitchChecked,
        onSwitchCheckedChange = viewModel::onSwitchCheckedChange,
        onBackClick = viewModel::onBackClick,
        onLinkClick = viewModel::onLinkClick,
        isSubscribeButtonEnabled = isSubscribeButtonEnabled,
        onSubscribeClick = viewModel::onSubscribeClick,
        isInputEnabled = isInputEnabled,
        isLoaderVisible = isLoaderVisible,
    )
}

@Composable
fun SubscribeScreenBehavior(
    sideEffects: Flow<SubscribeViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(sideEffects, context) {
        sideEffects.collect { effect ->
            when (effect) {
                SubscribeViewModel.SideEffect.GoBack -> goBack()
                is SubscribeViewModel.SideEffect.ShowBrowser -> context.openBrowser(effect.url)
            }
        }
    }
}

private const val ANNOTATION_TAG_URL = "url"

@Preview
@Composable
fun SubscribeScreenContentPreview() {
    ZarinaTheme {
        var isSwitchChecked by remember { mutableStateOf(false) }
        SubscribeScreenContent(
            name = "Михаил",
            nameError = null,
            onNameChange = {},
            onNameFocusChange = {},
            email = "mikhail@gmail.com",
            emailError = null,
            onEmailChange = {},
            onEmailFocusChange = {},
            isSwitchChecked = isSwitchChecked,
            onSwitchCheckedChange = { isSwitchChecked = it },
            onBackClick = {},
            onLinkClick = {},
            isSubscribeButtonEnabled = true,
            onSubscribeClick = {},
            isInputEnabled = true,
            isLoaderVisible = false,
        )
    }
}
