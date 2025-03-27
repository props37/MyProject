package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uikit.tag.ZarinaTag
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.Filter
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.ToggleableFilter
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun Filtration(
    filterTextFieldState: TextFieldState,
    filters: ImmutableList<ToggleableFilter>,
    onFilterClicked: (ToggleableFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        var focusState by remember { mutableStateOf<FocusState?>(null) }

        ZarinaTextField(
            state = filterTextFieldState,
            lineLimits = TextFieldLineLimits.SingleLine,
            leadingContent = {
                Icon(
                    imageVector = ImageVector.vectorResource(RCommon.drawable.ic_magnifying_glass_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            },
            placeholder = {
                Text(text = stringResource(R.string.cart_address_or_name))
            },
            innerTrailingContent = {
                ZarinaTextFieldDefaults.ClearButton(
                    isVisible = filterTextFieldState.text.isNotBlank(),
                    onClick = { filterTextFieldState.clearText() },
                )
            },
            outerTrailingContent = {
                val focusManager = LocalFocusManager.current
                ZarinaTextFieldDefaults.CancelButton(
                    isVisible = focusState?.isFocused == true,
                    onClick = { focusManager.clearFocus() },
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .onFocusChanged { focusState = it },
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        ) {
            val startEndPadding = 8.dp
            Spacer(modifier = Modifier.width(startEndPadding))

            filters.forEach { filter ->
                key(filter.filter) {
                    ZarinaTag(
                        onClick = { onFilterClicked(filter) },
                        isSelected = filter.isApplied,
                    ) {
                        val textResId = when (filter.filter) {
                            Filter.PAYMENT_BY_CARD -> R.string.cart_payment_by_card
                            Filter.FITTING -> R.string.cart_fitting_available
                        }

                        Text(text = stringResource(textResId))
                    }
                }
            }

            Spacer(modifier = Modifier.width(startEndPadding))
        }
    }
}
