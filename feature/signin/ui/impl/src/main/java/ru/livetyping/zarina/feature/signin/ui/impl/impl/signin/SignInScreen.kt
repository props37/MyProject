package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.YandexCaptchaEvent
import ru.livetyping.zarina.core.uicomponent.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerConnectedToTabRowState
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaDialog
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.component.SignInTopBar
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.component.SignInTypePager
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.component.SignInTypeSelector
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInEvent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInType

@Composable
internal fun SignInScreen(
    navActions: SignInNavActions,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val signInTypeSelectorState by viewModel.signInTypeSelectorState.collectAsStateWithLifecycle()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()

    ScreenContent(
        signInTypeSelectorState = signInTypeSelectorState,
        onSignInTypeSelectorEvent = viewModel::onSignInTypeSelectorEvent,
        signInState = signInState,
        onSignInEvent = viewModel::onSignInEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        onYandexCaptchaEvent = viewModel::onYandexCaptchaEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    signInTypeSelectorState: TabRowState<SignInType>,
    onSignInTypeSelectorEvent: (TabRowEvent<SignInType>) -> Unit,
    signInState: SignInState,
    onSignInEvent: (SignInEvent) -> Unit,
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    onYandexCaptchaEvent: (YandexCaptchaEvent) -> Unit,
    sideEffects: Flow<SignInSideEffect>,
    navActions: SignInNavActions,
) {
    SignInScreenBehavior(
        onLifecycleEvent = onLifecycleEvent,
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Box {
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
            SignInTopBar(
                onBackClicked = { onSignInEvent(SignInEvent.BackClicked) },
            )

            val signInTypePagerState = rememberPagerConnectedToTabRowState(
                tabs = signInTypeSelectorState.tabs,
                currentTab = signInTypeSelectorState.currentTab,
                onTabChanged = { onSignInTypeSelectorEvent(TabRowEvent.TabChanged(it)) },
                initialPage = remember { signInTypeSelectorState.currentTabIndex },
                pageCount = { signInTypeSelectorState.tabs.size },
            )

            SignInTypeSelector(
                state = signInTypeSelectorState,
                onEvent = onSignInTypeSelectorEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            SignInTypePager(
                signInTypeSelectorState = signInTypeSelectorState,
                pagerState = signInTypePagerState,
                signInState = signInState,
                onSignInEvent = onSignInEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (signInState.visibleYandexCaptcha != null) {
            YandexCaptchaDialog(
                captcha = signInState.visibleYandexCaptcha,
                onEvent = onYandexCaptchaEvent,
            )
        }
    }
}
