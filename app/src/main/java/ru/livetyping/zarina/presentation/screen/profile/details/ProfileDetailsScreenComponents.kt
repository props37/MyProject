package ru.livetyping.zarina.presentation.screen.profile.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.switchh.ZarinaSwitch
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldSize
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.datetime.DateTimeUtils
import ru.livetyping.zarina.presentation.common.util.rememberFormattedLocalDate
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPhoneNumber
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.State
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.text.rememberStringWithLinks
import ru.livetyping.zarina.util.compose.tryRequestFocus
import ru.livetyping.zarina.util.kotlin.date.LocalDateUtil
import java.time.ZoneOffset

@Suppress("ConstPropertyName")
object ProfileDetailsScreenComponents {

    @Composable
    fun TopBar(
        isSaveUserInfoButtonVisible: Boolean,
        onSaveUserInfoClicked: () -> Unit,
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.account_details),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            endContent = {
                AnimatedVisibility(
                    visible = isSaveUserInfoButtonVisible,
                    enter = AnimatedContentDefaultEnterTransition,
                    exit = AnimatedContentDefaultExitTransition,
                ) {
                    ZarinaButton(
                        onClick = onSaveUserInfoClicked,
                        size = ZarinaButtonSize.Medium,
                        colors = ZarinaButtonDefaults.backlessColors(),
                        modifier = Modifier
                            .heightIn(min = 40.dp)
                            .padding(end = 8.dp),
                    ) {
                        Text(text = stringResource(R.string.save).uppercase())
                    }
                }
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun ProfileDetails(
        state: State,
        firstNameTextFieldState: TextFieldState,
        lastNameTextFieldState: TextFieldState,
        birthDateMillisUtc: Long?,
        isBrithDateChangeable: Boolean,
        onBirthDateMillisClicked: () -> Unit,
        phoneNumber: String?,
        onPhoneNumberClicked: () -> Unit,
        email: String,
        onEmailClicked: () -> Unit,
        receiveEmails: Boolean,
        onReceiveEmailsChanged: (Boolean) -> Unit,
        receiveSms: Boolean,
        onReceiveSmsChanged: (Boolean) -> Unit,
        onChangePasswordClicked: () -> Unit,
        onRemoteUserErrorRefreshClicked: () -> Unit,
        onSignOutClicked: () -> Unit,
        onDeleteAccountClicked: () -> Unit,
        onUrlClicked: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is State.Success -> ProfileDetailsContentKeySuccess
                    State.Loading, is State.Error -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is State.Success -> {
                    ProfileDetailsImpl(
                        firstNameTextFieldState = firstNameTextFieldState,
                        lastNameTextFieldState = lastNameTextFieldState,
                        birthDateMillisUtc = birthDateMillisUtc,
                        isBrithDateChangeable = isBrithDateChangeable,
                        onBirthDateMillisClicked = onBirthDateMillisClicked,
                        phoneNumber = phoneNumber,
                        onPhoneNumberClicked = onPhoneNumberClicked,
                        email = email,
                        onEmailClicked = onEmailClicked,
                        receiveEmails = receiveEmails,
                        onReceiveEmailsChanged = onReceiveEmailsChanged,
                        receiveSms = receiveSms,
                        onReceiveSmsChanged = onReceiveSmsChanged,
                        onChangePasswordClicked = onChangePasswordClicked,
                        onSignOutClicked = onSignOutClicked,
                        onDeleteAccountClicked = onDeleteAccountClicked,
                        onUrlClicked = onUrlClicked,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                State.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                    }
                }

                is State.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onRemoteUserErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ProfileDetailsImpl(
        firstNameTextFieldState: TextFieldState,
        lastNameTextFieldState: TextFieldState,
        birthDateMillisUtc: Long?,
        isBrithDateChangeable: Boolean,
        onBirthDateMillisClicked: () -> Unit,
        phoneNumber: String?,
        onPhoneNumberClicked: () -> Unit,
        email: String,
        onEmailClicked: () -> Unit,
        receiveEmails: Boolean,
        onReceiveEmailsChanged: (Boolean) -> Unit,
        receiveSms: Boolean,
        onReceiveSmsChanged: (Boolean) -> Unit,
        onChangePasswordClicked: () -> Unit,
        onSignOutClicked: () -> Unit,
        onDeleteAccountClicked: () -> Unit,
        onUrlClicked: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(8.dp))

            PersonalDataBlock(
                firstNameTextFieldState = firstNameTextFieldState,
                lastNameTextFieldState = lastNameTextFieldState,
                birthDateMillisUtc = birthDateMillisUtc,
                isBrithDateChangeable = isBrithDateChangeable,
                onBirthDateClicked = onBirthDateMillisClicked,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactsBlock(
                phoneNumber = phoneNumber,
                onPhoneNumberClicked = onPhoneNumberClicked,
                email = email,
                onEmailClicked = onEmailClicked,
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsBlock(
                onChangePasswordClicked = onChangePasswordClicked,
                receiveEmails = receiveEmails,
                onReceiveEmailsChanged = onReceiveEmailsChanged,
                receiveSms = receiveSms,
                onReceiveSmsChanged = onReceiveSmsChanged,
                onUrlClicked = onUrlClicked,
            )

            Spacer(modifier = Modifier.height(32.dp))

            ZarinaButton(
                onClick = onSignOutClicked,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.sign_out).uppercase())
            }

            Spacer(modifier = Modifier.height(8.dp))

            ZarinaButton(
                onClick = onDeleteAccountClicked,
                colors = ZarinaButtonDefaults.backlessErrorColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.delete_account).uppercase())
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    @Composable
    private fun PersonalDataBlock(
        firstNameTextFieldState: TextFieldState,
        lastNameTextFieldState: TextFieldState,
        birthDateMillisUtc: Long?,
        isBrithDateChangeable: Boolean,
        onBirthDateClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val itemModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

            BlockTitle(
                text = stringResource(R.string.personal_data),
                modifier = itemModifier,
            )

            Spacer(modifier = Modifier.height(8.dp))

            val lastNameFocusRequester = remember { FocusRequester() }
            ZarinaTextField(
                state = lastNameTextFieldState,
                size = ZarinaTextFieldSize.Small,
                label = {
                    Text(text = stringResource(R.string.last_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.last_name_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = lastNameTextFieldState.text.isNotEmpty(),
                        onClick = {
                            lastNameTextFieldState.clearText()
                            lastNameFocusRequester.tryRequestFocus()
                        },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = itemModifier.focusRequester(lastNameFocusRequester),
            )

            Spacer(modifier = Modifier.height(16.dp))

            val firstNameFocusRequester = remember { FocusRequester() }
            ZarinaTextField(
                state = firstNameTextFieldState,
                size = ZarinaTextFieldSize.Small,
                textStyle = ItemBodyTextStyle,
                label = {
                    Text(text = stringResource(R.string.first_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.first_name_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = firstNameTextFieldState.text.isNotEmpty(),
                        onClick = {
                            firstNameTextFieldState.clearText()
                            firstNameFocusRequester.tryRequestFocus()
                        },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = itemModifier.focusRequester(firstNameFocusRequester),
            )

            Spacer(modifier = Modifier.height(16.dp))

            val formattedDate = if (birthDateMillisUtc != null) {
                rememberFormattedLocalDate(
                    localDate = remember(birthDateMillisUtc) {
                        LocalDateUtil.fromMillis(birthDateMillisUtc, ZoneOffset.UTC)
                    },
                    formatterPattern = DateTimeUtils.DATE_FORMAT_PATTERN,
                )
            } else ""
            ZarinaTextField(
                value = formattedDate,
                onValueChanged = {},
                isEnabled = false,
                size = ZarinaTextFieldSize.Small,
                textStyle = ItemBodyTextStyle,
                label = {
                    Text(text = stringResource(R.string.birth_date_text_field_label))
                },
                placeholder = {
                    Text(text = stringResource(R.string.birth_date_text_field_placeholder))
                },
                colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = onBirthDateClicked,
                        enabled = isBrithDateChangeable,
                    )
                    .padding(horizontal = 16.dp),
            )
        }
    }

    @Composable
    private fun ContactsBlock(
        phoneNumber: String?,
        onPhoneNumberClicked: () -> Unit,
        email: String,
        onEmailClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val itemModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

            BlockTitle(
                text = stringResource(R.string.contacts),
                modifier = itemModifier,
            )

            Spacer(modifier = Modifier.height(8.dp))

            val phoneTitle = if (phoneNumber != null) stringResource(R.string.phone) else null
            val phoneBody = if (phoneNumber != null) {
                rememberFormattedPhoneNumber(phoneNumber) ?: phoneNumber
            } else {
                stringResource(R.string.phone)
            }
            BlockItem(
                title = phoneTitle,
                body = phoneBody,
                onClick = onPhoneNumberClicked,
                endContent = {
                    BlockItemEndArrow()
                },
                modifier = Modifier.fillMaxWidth(),
            )

            ZarinaDivider(modifier = itemModifier)

            Spacer(modifier = Modifier.height(16.dp))

            BlockItem(
                title = stringResource(R.string.email),
                body = email,
                onClick = onEmailClicked,
                endContent = {
                    BlockItemEndArrow()
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    @Composable
    private fun SettingsBlock(
        onChangePasswordClicked: () -> Unit,
        receiveEmails: Boolean,
        onReceiveEmailsChanged: (Boolean) -> Unit,
        receiveSms: Boolean,
        onReceiveSmsChanged: (Boolean) -> Unit,
        onUrlClicked: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val itemModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

            BlockTitle(
                text = stringResource(R.string.settings),
                modifier = itemModifier,
            )
            Spacer(modifier = Modifier.height(8.dp))

            BlockItem(
                title = null,
                body = stringResource(R.string.change_password),
                onClick = onChangePasswordClicked,
                endContent = {
                    BlockItemEndArrow()
                },
                modifier = Modifier.fillMaxWidth(),
            )
            ZarinaDivider(modifier = itemModifier)

            BlockItem(
                title = null,
                body = stringResource(R.string.receive_news_by_email),
                onClick = { onReceiveEmailsChanged(!receiveEmails) },
                endContent = {
                    ZarinaSwitch(
                        isChecked = receiveEmails,
                        onCheckedChanged = onReceiveEmailsChanged,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            ZarinaDivider(modifier = itemModifier)

            BlockItem(
                title = null,
                body = stringResource(R.string.receive_sms_notifications),
                description = stringResource(
                    id = R.string.notifications_about_order_statuses_will_continue_to_arrive,
                ),
                onClick = { onReceiveSmsChanged(!receiveSms) },
                endContent = {
                    ZarinaSwitch(
                        isChecked = receiveSms,
                        onCheckedChanged = onReceiveSmsChanged,
                    )
                },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))

            SubscriptionPolicy(
                onUrlClicked = onUrlClicked,
                modifier = itemModifier,
            )
        }
    }

    @Composable
    private fun BlockTitle(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            modifier = modifier.heightIn(min = 48.dp),
            contentPadding = PaddingValues(),
        ) {
            Text(
                text = text,
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
            )
        }
    }

    @Composable
    private fun BlockItem(
        title: String?,
        body: String,
        endContent: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        description: String? = null,
        onClick: (() -> Unit)? = null,
        contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
    ) {
        ZarinaItem(
            onClick = onClick,
            startContent = {
                Column {
                    if (title != null) {
                        Text(
                            text = title,
                            style = UiKitTheme.typography.footnote.light,
                            color = UiKitTheme.colors.text.general.regular.muted,
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = body,
                        style = ItemBodyTextStyle,
                        color = UiKitTheme.colors.text.general.regular.default,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (description != null) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = description,
                            style = UiKitTheme.typography.footnote.light,
                            color = UiKitTheme.colors.text.general.regular.muted,
                        )
                    }
                }
            },
            endContent = {
                endContent()
            },
            contentPadding = contentPadding,
            modifier = modifier,
        )
    }

    @Composable
    private fun BlockItemEndArrow(
        modifier: Modifier = Modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
            contentDescription = null,
            modifier = modifier
                .size(16.dp)
                .rotate(degrees = 90f),
        )
    }

    @Composable
    private fun SubscriptionPolicy(
        onUrlClicked: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val baseString =
            stringResource(R.string.by_subscribing_to_newsletter_you_agree_to_personal_data_policies)
        val personalDataPolicyText =
            stringResource(R.string.by_subscribing_to_newsletter_you_agree_to_personal_data_policies_policy_text)
        val personalDataPolicyUrl =
            stringResource(R.string.personal_data_policy_url)

        val substringToUrl = remember(personalDataPolicyText, personalDataPolicyUrl) {
            mapOf(personalDataPolicyText to personalDataPolicyUrl)
        }

        val text = rememberStringWithLinks(
            baseString = baseString,
            substringToUrl = substringToUrl,
            urlStyle = UiKitTheme.typography.footnote.regular.toSpanStyle(),
            onUrlClicked = onUrlClicked,
        )

        Text(
            text = text,
            style = UiKitTheme.typography.footnote.light,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = modifier,
        )
    }

    const val DatePickerMinYear = 1900

    private const val ProfileDetailsContentKeySuccess = "ProfileDetailsContentKeySuccess"

    private val ItemBodyTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light
}
