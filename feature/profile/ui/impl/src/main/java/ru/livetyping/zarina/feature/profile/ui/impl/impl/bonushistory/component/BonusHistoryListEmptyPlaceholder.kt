package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.model.BonusHistoryTab

@Composable
internal fun BonusHistoryListEmptyPlaceholder(
    tab: BonusHistoryTab,
    modifier: Modifier = Modifier,
) {
    val titleResId = when (tab) {
        BonusHistoryTab.BONUS_HISTORY -> R.string.profile_bonus_history_is_empty
        BonusHistoryTab.EXPECTED_BONUSES -> R.string.profile_expected_bonuses_list_is_empty
    }
    val bodyResId = when (tab) {
        BonusHistoryTab.BONUS_HISTORY -> R.string.profile_bonus_history_will_be_displayed_here
        BonusHistoryTab.EXPECTED_BONUSES -> R.string.profile_expected_bonuses_will_be_displayed_here
    }
    val textColor = UiKitTheme.colors.text.general.regular.default

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(titleResId),
            style = UiKitTheme.typography.primary.bold,
            color = textColor,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(bodyResId),
            style = UiKitTheme.typography.secondary.regular,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}
