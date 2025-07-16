package ru.livetyping.zarina.feature.catalog.ui.impl.screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.feature.catalog.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SearchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(1.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .minimumInteractiveComponentSize()
            .padding(contentPadding),
    ) {
        val text = stringResource(R.string.catalog_search)

        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_magnifying_glass_24),
            contentDescription = text,
            tint = UiKitTheme2.colors.mainBlack,
            modifier = Modifier.size(16.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text.uppercase(),
            style = UiKitTheme2.typography.body2,
            color = UiKitTheme2.colors.mainBlack,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        SearchButton(
            onClick = {},
            modifier = Modifier.background(Color.White),
        )
    }
}
