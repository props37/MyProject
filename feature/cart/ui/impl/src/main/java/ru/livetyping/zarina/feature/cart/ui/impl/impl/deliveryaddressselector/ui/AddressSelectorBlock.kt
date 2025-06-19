package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.feature.cart.ui.impl.R

@Composable
internal fun AddressSelectorBlock(
    streetSelectorTextFieldState: TextFieldState,
    buildingSelectorTextFieldState: TextFieldState,
    apartmentSelectorTextFieldState: TextFieldState,
    isBuildingSelectorClickable: Boolean,
    onStreetSelectorClicked: () -> Unit,
    onBuildingSelectorClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        val streetInteractionSource = remember { MutableInteractionSource() }
        LaunchedEffect(streetInteractionSource) {
            streetInteractionSource.interactions.collect {
                if (it is PressInteraction.Release) {
                    onStreetSelectorClicked()
                }
            }
        }

        ZarinaTextField(
            state = streetSelectorTextFieldState,
            isEnabled = false,
            label = {
                val text = if (streetSelectorTextFieldState.text.isNotEmpty()) {
                    stringResource(R.string.cart_street)
                } else ""
                Text(text = text.uppercase())
            },
            placeholder = {
                Text(text = stringResource(R.string.cart_street).uppercase())
            },
            colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
            interactionSource = streetInteractionSource,
            modifier = Modifier
                .indication(streetInteractionSource, ripple())
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            val buildingInteractionSource = remember { MutableInteractionSource() }
            LaunchedEffect(buildingInteractionSource, isBuildingSelectorClickable) {
                buildingInteractionSource.interactions.collect {
                    if (it is PressInteraction.Release && isBuildingSelectorClickable) {
                        onBuildingSelectorClicked()
                    }
                }
            }
            val buildingIndicationModifier = if (isBuildingSelectorClickable) {
                Modifier.indication(buildingInteractionSource, ripple())
            } else Modifier

            ZarinaTextField(
                state = buildingSelectorTextFieldState,
                isEnabled = false,
                label = {
                    val text = if (buildingSelectorTextFieldState.text.isNotEmpty()) {
                        stringResource(R.string.cart_building)
                    } else ""
                    Text(text = text.uppercase())
                },
                placeholder = {
                    Text(text = stringResource(R.string.cart_building).uppercase())
                },
                colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                interactionSource = buildingInteractionSource,
                modifier = Modifier
                    .weight(1f)
                    .then(buildingIndicationModifier)
                    .padding(horizontal = 16.dp),
            )

            ZarinaTextField(
                state = apartmentSelectorTextFieldState,
                label = {
                    val text = if (apartmentSelectorTextFieldState.text.isNotEmpty()) {
                        stringResource(R.string.cart_apartment_or_office)
                    } else ""
                    Text(text = text.uppercase())
                },
                placeholder = {
                    Text(text = stringResource(R.string.cart_apartment_or_office).uppercase())
                },
                colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                onKeyboardAction = { default ->
                    focusManager.clearFocus()
                    default()
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            )
        }
    }
}
