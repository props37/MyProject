package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun RedirectSearchBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SearchBar(
        onClick = onClick,
        isInputEnabled = false,
        value = "",
        onValueChange = {},
        onClearClick = {},
        modifier = modifier
    )
}

@Composable
private fun SearchBar(
    onClick: () -> Unit,
    isInputEnabled: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_search_24),
            contentDescription = stringResource(id = R.string.search),
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .padding(vertical = 10.dp)
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SearchInput(
            value = value,
            onValueChange = onValueChange,
            isEnabled = isInputEnabled,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        AnimatedVisibility(
            visible = value.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_close_24),
                contentDescription = stringResource(id = R.string.clear),
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onClearClick)
                    .minimumInteractiveComponentSize()
                    .padding(vertical = 10.dp),
            )
        }
    }
}

@Composable
private fun SearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    // TODO IME actions
    Box(modifier = modifier.fillMaxWidth()) {
        val interactionSource = remember { MutableInteractionSource() }
        AnimatedVisibility(
            visible = value.isEmpty(),
            label = "search input hint",
            enter = fadeIn(),
            exit = ExitTransition.None,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.search_products),
                style = UiKitTheme.typography.circle1518,
                color = UiKitTheme.colors.hint,
                maxLines = 1,
                textAlign = TextAlign.Start,
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = UiKitTheme.typography.circle1518.copy(color = UiKitTheme.colors.primaryContentColor),
            singleLine = true,
            enabled = isEnabled,
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    ZarinaTheme {
        var value by remember { mutableStateOf("") }
        SearchBar(
            onClick = {},
            value = value,
            onValueChange = { value = it },
            isInputEnabled = true,
            onClearClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}