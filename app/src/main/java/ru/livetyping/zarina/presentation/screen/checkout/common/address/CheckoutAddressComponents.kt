package ru.livetyping.zarina.presentation.screen.checkout.common.address

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults

object CheckoutAddressComponents {

    @Composable
    fun AddressBlock(
        streetTextFieldState: TextFieldState,
        buildingTextFieldState: TextFieldState,
        apartmentTextFieldState: TextFieldState,
        onStreetClicked: () -> Unit,
        onBuildingClicked: () -> Unit,
        onApartmentClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val streetInteractionSource = remember { MutableInteractionSource() }
            LaunchedEffect(streetInteractionSource) {
                streetInteractionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        onStreetClicked()
                    }
                }
            }

            ZarinaTextField(
                state = streetTextFieldState,
                isEnabled = false,
                label = {
                    val text = if (streetTextFieldState.text.isNotEmpty()) {
                        stringResource(R.string.street)
                    } else ""
                    Text(text = text)
                },
                placeholder = {
                    Text(text = stringResource(R.string.street))
                },
                colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                lineLimits = TextFieldLineLimits.SingleLine,
                interactionSource = streetInteractionSource,
                modifier = Modifier
                    .indication(streetInteractionSource, ripple())
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                val buildingInteractionSource = remember { MutableInteractionSource() }
                LaunchedEffect(buildingInteractionSource) {
                    buildingInteractionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onBuildingClicked()
                        }
                    }
                }

                ZarinaTextField(
                    state = buildingTextFieldState,
                    isEnabled = false,
                    label = {
                        val text = if (buildingTextFieldState.text.isNotEmpty()) {
                            stringResource(R.string.building)
                        } else ""
                        Text(text = text)
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.building))
                    },
                    colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    interactionSource = buildingInteractionSource,
                    modifier = Modifier
                        .weight(1f)
                        .indication(buildingInteractionSource, ripple())
                        .padding(horizontal = 16.dp),
                )

                val apartmentInteractionSource = remember { MutableInteractionSource() }
                LaunchedEffect(apartmentInteractionSource) {
                    apartmentInteractionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onApartmentClicked()
                        }
                    }
                }

                ZarinaTextField(
                    state = apartmentTextFieldState,
                    isEnabled = false,
                    label = {
                        val text = if (apartmentTextFieldState.text.isNotEmpty()) {
                            stringResource(R.string.apartment_slash_office)
                        } else ""
                        Text(text = text)
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.apartment_slash_office))
                    },
                    colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    interactionSource = apartmentInteractionSource,
                    modifier = Modifier
                        .weight(1f)
                        .indication(apartmentInteractionSource, ripple())
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}
