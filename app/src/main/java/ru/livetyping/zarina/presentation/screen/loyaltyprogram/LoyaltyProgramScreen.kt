package ru.livetyping.zarina.presentation.screen.loyaltyprogram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramScreenComponents.BonusHistory
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramScreenComponents.LoyaltyProgramInfo
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramScreenComponents.LoyaltyProgramPolicies
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun LoyaltyProgramScreen(
    navigate: (LoyaltyProgramScreenAction) -> Unit,
    viewModel: LoyaltyProgramViewModel = hiltViewModel(),
) {
    val loyaltyCard by viewModel.loyaltyCard.collectAsStateWithLifecycle()

    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        loyaltyCard = loyaltyCard,
        onLoyaltyProgramPoliciesClicked = viewModel::onLoyaltyProgramPoliciesClicked,
        onBonusHistoryClicked = viewModel::onBonusHistoryClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    loyaltyCard: LoyaltyCard?,
    onLoyaltyProgramPoliciesClicked: () -> Unit,
    onBonusHistoryClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (LoyaltyProgramScreenAction) -> Unit,
) {
    LoyaltyProgramScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        TopBar(onBackClicked = onBackClicked)

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            LoyaltyProgramInfo(loyaltyCard)
            LoyaltyProgramPolicies(onClick = onLoyaltyProgramPoliciesClicked)
            BonusHistory(onClick = onBonusHistoryClicked)

            Spacer(modifier = Modifier.height(20.dp))
            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarHeight))
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
