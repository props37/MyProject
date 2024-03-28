package ru.zarina.zarina.ui.common.component.textfield

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.button.ZarinaIconButton
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

@Composable
fun ZarinaPasswordTextField(
    password: String,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.password), // TODO: [High] Update
    placeholder: String = stringResource(R.string.password), // TODO: [High] Update
) {
    var isPasswordHidden by remember { mutableStateOf(true) }
    val visualTransformation = remember(isPasswordHidden) {
        if (isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None
    }

    ZarinaTextField(
        value = password,
        onValueChanged = onPasswordChanged,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        innerTrailingContent = {
            AnimatedContent(
                targetState = isPasswordHidden,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec().using(sizeTransform = null)
                },
                contentAlignment = Alignment.Center,
                label = "PasswordTextField eye icon",
            ) { isPasswordHiddenValue ->
                ZarinaIconButton(
                    onClick = { isPasswordHidden = !isPasswordHidden },
                    indication = rememberRipple(bounded = false, radius = 16.dp),
                    modifier = Modifier.size(36.dp),
                ) {
                    val iconResId: Int
                    val contentDescriptionResId: Int
                    if (isPasswordHiddenValue) {
                        iconResId = R.drawable.ic_eye_open_24
                        contentDescriptionResId = R.string.show_password
                    } else {
                        iconResId = R.drawable.ic_eye_closed_24
                        contentDescriptionResId = R.string.hide_password
                    }

                    Icon(
                        painter = painterResource(iconResId),
                        contentDescription = stringResource(contentDescriptionResId),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        },
        visualTransformation = visualTransformation,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var password by remember { mutableStateOf("") }

        ZarinaPasswordTextField(
            password = password,
            onPasswordChanged = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
