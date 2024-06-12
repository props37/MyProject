package ru.livetyping.zarina.presentation.screen.loyaltyprogram

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.util.domain.nameResId
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

object LoyaltyProgramScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(text = stringResource(R.string.loyalty_program))
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun LoyaltyProgramInfo(
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

        val body = @Composable { text: String? ->
            @Suppress("NAME_SHADOWING")
            Crossfade(
                targetState = text,
                contentKey = { it != null },
            ) { text ->
                if (text != null) {
                    Text(text = text)
                } else {
                    Text(text = "")
                    ZarinaTextSkeleton(
                        textStyle = LoyaltyProgramInfoItemBodyTextStyle,
                        modifier = Modifier.width(120.dp),
                    )
                }
            }
        }

        Column(modifier = modifier) {
            LoyaltyProgramInfoItem(
                title = stringResource(R.string.loyalty_program_level),
                body = {
                    val text = loyaltyCard?.let { stringResource(it.level.nameResId) }
                    body(text)
                },
            )
            divider()

            LoyaltyProgramInfoItem(
                title = stringResource(R.string.my_bonuses),
                body = {
                    val text = loyaltyCard?.let {
                        pluralStringResource(
                            id = R.plurals.d_bonuses,
                            count = it.bonuses.bonusCount,
                            it.bonuses.bonusCount,
                        )
                    }
                    body(text)
                },
            )
            divider()

            LoyaltyProgramInfoItem(
                title = stringResource(R.string.expected_bonuses),
                body = {
                    val text = loyaltyCard?.let {
                        pluralStringResource(
                            id = R.plurals.d_bonuses,
                            count = it.bonuses.expectedBonusCount,
                            it.bonuses.expectedBonusCount,
                        )
                    }
                    body(text)
                },
            )
            divider()
        }
    }

    @Composable
    fun LoyaltyProgramPolicies(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem(
                onClick = onClick,
                startContent = {
                    Text(
                        text = stringResource(id = R.string.loyalty_program_policies),
                        style = LoyaltyProgramInfoItemBodyTextStyle,
                    )
                },
                endContent = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(degrees = 90f),
                    )
                },
            )

            ZarinaDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }

    @Composable
    fun BonusHistory(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            onClick = onClick,
            startContent = {
                Text(
                    text = stringResource(id = R.string.bonus_history),
                    style = LoyaltyProgramInfoItemBodyTextStyle,
                )
            },
            endContent = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(degrees = 90f),
                )
            },
            modifier = modifier,
        )
    }

    @Composable
    private fun LoyaltyProgramInfoItem(
        title: String,
        body: @Composable () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Column {
                Text(
                    text = title,
                    style = UiKitTheme.typography.footnote.light,
                    color = UiKitTheme.colors.text.general.regular.muted,
                )
                Spacer(modifier = Modifier.height(2.dp))
                CompositionLocalProvider(
                    LocalTextStyle provides LoyaltyProgramInfoItemBodyTextStyle,
                    LocalContentColor provides UiKitTheme.colors.text.general.regular.default,
                    content = body,
                )
            }
        }
    }

    private val LoyaltyProgramInfoItemBodyTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light
}
