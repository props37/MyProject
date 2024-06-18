package ru.livetyping.zarina.presentation.screen.profile.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.theme.UiKitTheme

object ProfileDetailsScreenComponents {

    @Composable
    fun TopBar(
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
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun PersonalDataBlock(
        lastNameTextFieldState: TextFieldState,
        firstNameTextFieldState: TextFieldState,
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

            ZarinaTextField(
                state = lastNameTextFieldState,
                label = {
                    Text(text = stringResource(R.string.last_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.last_name_text_field_placeholder))
                },
                modifier = itemModifier,
            )

            ZarinaTextField(
                state = firstNameTextFieldState,
                label = {
                    Text(text = stringResource(R.string.first_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.first_name_text_field_placeholder))
                },
                modifier = itemModifier,
            )

            ZarinaTextField(
                state = rememberTextFieldState(),
                label = {
                    Text(text = stringResource(R.string.birth_date_text_field_label))
                },
                placeholder = {
                    Text(text = stringResource(R.string.birth_date_text_field_placeholder))
                },
                modifier = itemModifier,
            )
        }
    }

    @Composable
    fun ContactsBlock(
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

            ZarinaTextField(
                state = rememberTextFieldState(),
                label = {
                    Text(text = stringResource(R.string.phone))
                },
                placeholder = {
                    Text(text = stringResource(R.string.phone_text_field_placeholder))
                },
                modifier = itemModifier,
            )

            ZarinaTextField(
                state = rememberTextFieldState(),
                label = {
                    Text(text = stringResource(R.string.email))
                },
                placeholder = {
                    Text(text = stringResource(R.string.email_text_field_placeholder))
                },
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
        title: String,
        body: String,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            modifier = modifier,
            contentPadding = PaddingValues(),
        ) {
            Column {
                Text(
                    text = title,
                    style = UiKitTheme.typography.tertiary.light,
                    color = UiKitTheme.colors.text.general.regular.muted,
                )

                Text(
                    text = body,
                    style = UiKitTheme.typography.secondary.light,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            }
        }
    }
}
