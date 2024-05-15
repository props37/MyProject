package ru.livetyping.zarina.presentation.common.component.map

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize

object MapDefaults {

    @Composable
    fun MyLocationButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        elevation: Dp = 4.dp,
    ) {
        ZarinaButton(
            onClick = onClick,
            size = ZarinaButtonSize.Medium,
            colors = ZarinaButtonDefaults.secondaryColors(),
            contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
            modifier = modifier.shadow(elevation),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location_arrow_outline_24),
                contentDescription = stringResource(R.string.show_my_location),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
