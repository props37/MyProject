package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.platform.copyTextToClipboard
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.VersionDetails

@Composable
internal fun VersionDetails(
    details: ImmutableList<VersionDetails>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        details.forEach { item ->
            VersionDetailsItem(
                details = item,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun VersionDetailsItem(
    details: VersionDetails,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val text = remember(details, context, LocalConfiguration.current) {
        val title = details.title.getString(context)
        val version = details.version.getString(context)
        "$title $version"
    }

    Text(
        text = text,
        style = UiKitTheme.typography.footnote.regular,
        color = UiKitTheme.colors.text.general.regular.muted,
        modifier = modifier.clickable(
            interactionSource = null,
            indication = null,
            onClick = {
                val label = details.title.getString(context)
                val version = details.version.getString(context)
                context.copyTextToClipboard(label, version)
            }
        ),
    )
}
