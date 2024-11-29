package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.component

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
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.kotlinutil.LocalDateUtil
import ru.livetyping.zarina.core.uicommon.DateTimeUtils
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uicompose.rememberFormattedLocalDate
import ru.livetyping.zarina.core.uicompose.rememberFormattedPhoneNumber
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsState
import ru.livetyping.zarina.core.resource.R as RCommon

@Suppress("NAME_SHADOWING")
@Composable
internal fun ProfileDetailsContent(
    state: ProfileDetailsState,
    onEvent: (ProfileDetailsEvent) -> Unit,
    onBirthDateClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is ProfileDetailsState.Success -> ProfileDetailsContentKey.Success
                ProfileDetailsState.Loading -> it
                is ProfileDetailsState.Error -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is ProfileDetailsState.Success -> {
                ProfileDetailsImpl(
                    state = state,
                    onEvent = onEvent,
                    onBirthDateClicked = onBirthDateClicked,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            ProfileDetailsState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                }
            }

            is ProfileDetailsState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = { onEvent(ProfileDetailsEvent.ErrorRefreshClicked) },
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
    state: ProfileDetailsState.Success,
    onEvent: (ProfileDetailsEvent) -> Unit,
    onBirthDateClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(8.dp))

        PersonalData(
            firstNameTextFieldState = state.firstNameTextFieldState,
            lastNameTextFieldState = state.lastNameTextFieldState,
            birthDateEpochMillis = state.birthDateEpochMillis,
            isBirthDateClickable = state.isBirthDateChangeable,
            onBirthDateClicked = onBirthDateClicked,
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContactInfo(
            phone = state.phone,
            onPhoneClicked = { onEvent(ProfileDetailsEvent.PhoneClicked) },
            email = state.email,
            onEmailClicked = { onEvent(ProfileDetailsEvent.EmailClicked) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: [Top] Implement
        TODO()
    }
}

@Composable
private fun PersonalData(
    firstNameTextFieldState: TextFieldState,
    lastNameTextFieldState: TextFieldState,
    birthDateEpochMillis: Long?,
    isBirthDateClickable: Boolean,
    onBirthDateClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        val itemModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        BlockTitle(
            text = stringResource(R.string.profile_personal_data),
            modifier = itemModifier,
        )
        Spacer(modifier = Modifier.height(8.dp))

        ZarinaTextField(
            state = lastNameTextFieldState,
            textStyle = ItemBodyTextStyle,
            label = {
                val label = if (lastNameTextFieldState.text.isNotEmpty()) {
                    stringResource(RCommon.string.res_last_name)
                } else ""

                Text(text = label)
            },
            placeholder = {
                Text(text = stringResource(RCommon.string.res_last_name))
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
            modifier = itemModifier.focusRequester(lastNameFocusRequester),
        )

        Spacer(modifier = Modifier.height(16.dp))

        ZarinaTextField(
            state = firstNameTextFieldState,
            textStyle = ItemBodyTextStyle,
            label = {
                val label = if (firstNameTextFieldState.text.isNotEmpty()) {
                    stringResource(RCommon.string.res_first_name)
                } else ""

                Text(text = label)
            },
            placeholder = {
                Text(text = stringResource(RCommon.string.res_first_name))
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
            modifier = itemModifier.focusRequester(firstNameFocusRequester),
        )

        Spacer(modifier = Modifier.height(16.dp))

        val formattedDate = if (birthDateEpochMillis != null) {
            rememberFormattedLocalDate(
                localDate = remember(birthDateEpochMillis) {
                    LocalDateUtil.fromMillis(birthDateEpochMillis)
                },
                formatterPattern = DateTimeUtils.DATE_FORMAT_PATTERN,
            )
        } else ""

        ZarinaTextField(
            value = formattedDate,
            onValueChanged = {},
            isEnabled = false,
            textStyle = ItemBodyTextStyle,
            label = {
                val label = if (formattedDate.isNotEmpty()) {
                    stringResource(RCommon.string.res_birth_date)
                } else ""

                Text(text = label)
            },
            placeholder = {
                Text(text = stringResource(RCommon.string.res_birth_date))
            },
            colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onBirthDateClicked,
                    enabled = isBirthDateClickable,
                )
                .padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun ContactInfo(
    phone: PhoneNumber?,
    onPhoneClicked: () -> Unit,
    email: Email,
    onEmailClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val itemModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        BlockTitle(
            text = stringResource(R.string.profile_contacts),
            modifier = itemModifier,
        )
        Spacer(modifier = Modifier.height(8.dp))

        val phoneTitle = if (phone != null) stringResource(RCommon.string.res_phone) else null
        val phoneBody = if (phone != null) {
            rememberFormattedPhoneNumber(phone.value) ?: phone.value
        } else {
            stringResource(RCommon.string.res_phone)
        }
        BlockItem(
            title = phoneTitle,
            body = phoneBody,
            onClick = onPhoneClicked,
            endContent = {
                BlockItemEndArrow()
            },
            modifier = Modifier.fillMaxWidth(),
        )

        ZarinaDivider(modifier = itemModifier)

        Spacer(modifier = Modifier.height(16.dp))

        BlockItem(
            title = stringResource(RCommon.string.res_email),
            body = email.value,
            onClick = onEmailClicked,
            endContent = {
                BlockItemEndArrow()
            },
            modifier = Modifier.fillMaxWidth(),
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
        contentPadding = PaddingValues(vertical = 4.dp),
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
        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
        contentDescription = null,
        modifier = modifier
            .size(16.dp)
            .rotate(degrees = 90f),
    )
}

private val ItemBodyTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light

private enum class ProfileDetailsContentKey { Success }
