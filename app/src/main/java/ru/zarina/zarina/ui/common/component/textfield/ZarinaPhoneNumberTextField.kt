package ru.zarina.zarina.ui.common.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.util.compose.text.rememberPhoneNumberVisualTransformation

@Composable
fun ZarinaPhoneNumberTextField(
    phoneNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.phone),
    placeholder: String = stringResource(R.string.phone),
) {
    ZarinaTextField(
        value = phoneNumber,
        onValueChanged = onPhoneNumberChanged,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        keyboardOptions = remember { KeyboardOptions(keyboardType = KeyboardType.Phone) },
        singleLine = true,
        visualTransformation = rememberPhoneNumberVisualTransformation(),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var phone by remember { mutableStateOf("") }

        ZarinaPhoneNumberTextField(
            phoneNumber = phone,
            onPhoneNumberChanged = { phone = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
