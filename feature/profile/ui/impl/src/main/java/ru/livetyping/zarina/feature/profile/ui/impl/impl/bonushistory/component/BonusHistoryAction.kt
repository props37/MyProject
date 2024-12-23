package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.core.uicompose.price.rememberFormattedPrice
import ru.livetyping.zarina.core.uicompose.rememberFormattedLocalDate
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.R

@Composable
internal fun BonusHistoryAction(
    action: LoyaltyProgramBonusAction,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        startContent = {
            Box(contentAlignment = Alignment.CenterStart) {
                val typeTextResId = when (action.type) {
                    LoyaltyProgramBonusAction.Type.EARNED -> R.string.profile_accrual
                    LoyaltyProgramBonusAction.Type.SPENT -> R.string.profile_write_off
                }

                val date = action.date
                if (date != null) {
                    Column {
                        val formattedDate = rememberFormattedLocalDate(
                            localDate = date,
                            formatterPattern = DateFormatterPattern,
                        )

                        Text(
                            text = stringResource(typeTextResId),
                            style = BonusActionTitleTextStyle,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formattedDate,
                            style = BonusActionDescriptionTextStyle,
                            color = UiKitTheme.colors.text.general.regular.muted,
                        )
                    }
                } else {
                    Text(
                        text = stringResource(typeTextResId),
                        style = BonusActionTitleTextStyle,
                    )
                }
            }
        },
        endContent = {
            val formattedBonusCount = rememberFormattedPrice(action.bonusCount)
            val text = when (action.type) {
                LoyaltyProgramBonusAction.Type.EARNED -> "+$formattedBonusCount"
                LoyaltyProgramBonusAction.Type.SPENT -> "-$formattedBonusCount"
            }
            val style = when (action.type) {
                LoyaltyProgramBonusAction.Type.EARNED -> UiKitTheme.typography.secondary.regular
                LoyaltyProgramBonusAction.Type.SPENT -> UiKitTheme.typography.secondary.light
            }

            Text(
                text = text,
                style = style,
                color = UiKitTheme.colors.text.general.regular.muted,
            )
        },
        contentPadding = ContentPadding,
        modifier = modifier,
    )
}

@Composable
internal fun BonusHistoryActionSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        startContent = {
            Column {
                ZarinaTextSkeleton(
                    textStyle = BonusActionTitleTextStyle,
                    shimmer = shimmer,
                    useUiKitFontFamily = true,
                    modifier = Modifier.width(100.dp),
                )

                Spacer(modifier = Modifier.height(2.dp))

                ZarinaTextSkeleton(
                    textStyle = BonusActionDescriptionTextStyle,
                    shimmer = shimmer,
                    useUiKitFontFamily = true,
                    modifier = Modifier.width(120.dp),
                )
            }
        },
        endContent = {
            ZarinaTextSkeleton(
                textStyle = BonusActionTitleTextStyle,
                shimmer = shimmer,
                modifier = Modifier.width(80.dp),
            )
        },
        contentPadding = ContentPadding,
        modifier = modifier,
    )
}

private val BonusActionTitleTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light

private val BonusActionDescriptionTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.footnote.light

private const val DateFormatterPattern = "dd MMMM yyyy"

private val ContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
