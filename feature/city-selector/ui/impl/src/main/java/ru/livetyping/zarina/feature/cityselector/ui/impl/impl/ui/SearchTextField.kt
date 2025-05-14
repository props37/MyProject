package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.feature.cityselector.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SearchTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    ZarinaTextField(
        state = state,
        placeholder = {
            Text(text = stringResource(R.string.city_selector_city_name).uppercase())
        },
        leadingContent = {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_magnifying_glass_24),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
            )
        },
        modifier = modifier,
    )
}
