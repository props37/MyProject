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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.kotlinutil.LocalDateUtil
import ru.livetyping.zarina.core.uicommon.DateTimeUtils
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uicompose.rememberFormattedLocalDate
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
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

private enum class ProfileDetailsContentKey { Success }
