package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.item.ZarinaItemDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.feature.catalog.ui.impl.R
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuItem
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun MenuItemCity(
    item: MenuItem.City,
    onChangeClicked: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ZarinaItemDefaults.ContentPadding,
) {
    ZarinaItem(
        onClick = onChangeClicked,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_location_marker_24),
            contentDescription = stringResource(R.string.catalog_current_city, item.city.name),
            modifier = Modifier.size(16.dp),
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(text = item.city.name.uppercase())

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(RCommon.string.res_change).uppercase(),
            color = UiKitTheme2.colors.middleGray,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        MenuItemCity(
            item = MenuItem.City(City.getDefault()),
            onChangeClicked = {},
            modifier = Modifier.background(Color.White),
        )
    }
}
