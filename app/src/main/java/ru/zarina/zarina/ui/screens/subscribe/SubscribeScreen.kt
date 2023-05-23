package ru.zarina.zarina.ui.screens.subscribe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
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
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.form.Input
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.android.openBrowser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeScreenContent(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    isSwitchChecked: Boolean,
    onSwitchCheckedChange: (Boolean) -> Unit,
    onLinkClick: (SubscribeViewModel.Link) -> Unit,
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
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
            )
            Input(
                value = email,
                onValueChange = onEmailChange,
                hint = stringResource(R.string.email),
                modifier = Modifier
                    .padding(bottom = 8.dp)
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
                    modifier = Modifier.weight(1f),
                ) { index ->
                    val item = text
                        .getStringAnnotations(ANNOTATION_TAG_URL, index, index)
                        .firstOrNull()
                        ?.item
                    val link = item?.let { SubscribeViewModel.Link.valueOf(it) }
                    if (link != null) onLinkClick(link)
                }
            }
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
fun SubscribeScreen() {
    val viewModel = hiltViewModel<SubscribeViewModel>()

    val name by viewModel.name.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val isSwitchChecked by viewModel.isSwitchChecked.collectAsStateWithLifecycle()

    SubscribeScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SubscribeScreenContent(
        name = name,
        onNameChange = viewModel::onNameChange,
        email = email,
        onEmailChange = viewModel::onEmailChange,
        isSwitchChecked = isSwitchChecked,
        onSwitchCheckedChange = viewModel::onSwitchCheckedChange,
        onBackClick = viewModel::onBackClick,
        onLinkClick = viewModel::onLinkClick,
    )
}

@Composable
fun SubscribeScreenBehavior(
    sideEffects: Flow<SubscribeViewModel.SideEffect>,
) {
    val context = LocalContext.current
    LaunchedEffect(sideEffects, context) {
        sideEffects.collect { effect ->
            when (effect) {
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
            onNameChange = {},
            email = "mikhail@gmail.com",
            onEmailChange = {},
            isSwitchChecked = isSwitchChecked,
            onSwitchCheckedChange = { isSwitchChecked = it },
            onBackClick = {},
            onLinkClick = {},
        )
    }
}
