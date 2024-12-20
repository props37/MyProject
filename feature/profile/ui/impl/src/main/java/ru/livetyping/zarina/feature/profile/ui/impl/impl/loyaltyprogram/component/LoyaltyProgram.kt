package ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.model.LoyaltyProgramEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.model.LoyaltyProgramState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.util.nameResId
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun LoyaltyProgram(
    state: LoyaltyProgramState,
    onEvent: (LoyaltyProgramEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Suppress("NAME_SHADOWING")
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is LoyaltyProgramState.Success -> LoyaltyProgramContentKey.SuccessAndLoading
                LoyaltyProgramState.Loading -> LoyaltyProgramContentKey.SuccessAndLoading
                is LoyaltyProgramState.Error -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is LoyaltyProgramState.Success -> {
                LoyaltyProgramContent(
                    loyaltyCard = state.loyaltyCard,
                    onBonusHistoryClicked = { onEvent(LoyaltyProgramEvent.BonusHistoryClicked) },
                )
            }

            LoyaltyProgramState.Loading -> {
                LoyaltyProgramContent(
                    loyaltyCard = null,
                    onBonusHistoryClicked = { onEvent(LoyaltyProgramEvent.BonusHistoryClicked) },
                )
            }

            is LoyaltyProgramState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = { onEvent(LoyaltyProgramEvent.ErrorRefreshClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun LoyaltyProgramContent(
    loyaltyCard: LoyaltyCard?,
    onBonusHistoryClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val divider = @Composable {
        ZarinaDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        LoyaltyProgramInfo(loyaltyCard)

        divider()

        LoyaltyProgramPolicies()

        divider()

        BonusHistory(onClick = onBonusHistoryClicked)

        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
    }
}

@Composable
private fun LoyaltyProgramInfo(
    loyaltyCard: LoyaltyCard?,
    modifier: Modifier = Modifier,
) {
    val divider = @Composable {
        ZarinaDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }

    Column(modifier = modifier) {
        LoyaltyProgramInfoItem(
            headerText = stringResource(R.string.profile_loyalty_program_level),
            bodyText = loyaltyCard?.level?.nameResId?.let { stringResource(it) },
        )

        divider()

        LoyaltyProgramInfoItem(
            headerText = stringResource(R.string.profile_my_bonuses),
            bodyText = loyaltyCard?.bonuses?.bonusCount?.let { count ->
                pluralStringResource(
                    id = RCommon.plurals.res_bonus_count,
                    count = count,
                    count.toString(),
                )
            },
        )

        divider()

        LoyaltyProgramInfoItem(
            headerText = stringResource(R.string.profile_expected_bonuses),
            bodyText = loyaltyCard?.bonuses?.expectedBonusCount?.let { count ->
                pluralStringResource(
                    id = RCommon.plurals.res_bonus_count,
                    count = count,
                    count.toString(),
                )
            },
        )
    }
}

@Composable
private fun LoyaltyProgramPolicies(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val policiesUrl = stringResource(RCommon.string.res_zarina_loyalty_policy_url)

    ZarinaItem(
        onClick = { context.openUrlInCustomTabs(policiesUrl) },
        startContent = {
            Text(
                text = stringResource(R.string.profile_loyalty_program_policies),
                style = TextStyleDefault,
            )
        },
        endContent = {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(degrees = 90f),
            )
        },
        contentPadding = ItemContentPadding,
        modifier = modifier,
    )
}

@Composable
private fun BonusHistory(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = onClick,
        startContent = {
            Text(
                text = stringResource(R.string.profile_bonus_account_history),
                style = TextStyleDefault,
            )
        },
        endContent = {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(degrees = 90f),
            )
        },
        contentPadding = ItemContentPadding,
        modifier = modifier,
    )
}

@Composable
private fun LoyaltyProgramInfoItem(
    headerText: String,
    bodyText: String?,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = ItemContentPadding,
        modifier = modifier,
    ) {
        Column {
            Text(
                text = headerText,
                style = UiKitTheme.typography.footnote.light,
                color = UiKitTheme.colors.text.general.regular.muted,
            )

            Spacer(modifier = Modifier.height(2.dp))

            AnimatedContent(
                targetState = bodyText,
                transitionSpec = {
                    AnimatedContentCrossfadeTransitionSpec.using(SizeTransform(clip = false))
                },
                label = "LoyaltyProgramInfoItem body",
            ) { text ->
                if (text != null) {
                    Text(
                        text = text,
                        style = TextStyleDefault,
                    )
                } else {
                    ZarinaTextSkeleton(
                        textStyle = TextStyleDefault,
                        useUiKitFontFamily = true,
                        modifier = Modifier.width(80.dp),
                    )
                }
            }
        }
    }
}

private enum class LoyaltyProgramContentKey { SuccessAndLoading }

private val TextStyleDefault: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light

private val ItemContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
