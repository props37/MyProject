package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldSize
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBarDefaults
import ru.livetyping.zarina.core.uikit.R as RUiKit

@Composable
internal fun SearchBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(min = ZarinaTopBarDefaults.MinHeight)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
    ) {
        ZarinaTextField(
            value = "",
            onValueChanged = {},
            isEnabled = false,
            size = ZarinaTextFieldSize.Small,
            placeholder = {
                Text(text = stringResource(RUiKit.string.find_products))
            },
            leadingContent = {
                Icon(
                    imageVector = ImageVector.vectorResource(RUiKit.drawable.ic_magnifying_glass_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            },
            colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
        )
    }
}
