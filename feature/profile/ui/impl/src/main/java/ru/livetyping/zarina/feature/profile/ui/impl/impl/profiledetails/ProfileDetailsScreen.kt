package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.date.ZarinaDatePickerDialog
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.component.AccountDeletionDialog
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.component.ProfileDetailsContent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.component.ProfileDetailsTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.component.SignOutDialog
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.AccountDeletionDialogEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.AccountDeletionDialogState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsTopBarEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsTopBarState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.SignOutDialogEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.SignOutDialogState
import java.time.LocalDate

@Composable
internal fun ProfileDetailsScreen(
    navActions: ProfileDetailsNavActions,
    viewModel: ProfileDetailsViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val profileDetailsState by viewModel.profileDetailsState.collectAsStateWithLifecycle()
    val signOutDialogState by viewModel.signOutDialogState.collectAsStateWithLifecycle()
    val accountDeletionDialogState by viewModel.accountDeletionDialogState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        profileDetailsState = profileDetailsState,
        onProfileDetailsEvent = viewModel::onProfileDetailsEvent,
        signOutDialogState = signOutDialogState,
        onSignOutDialogEvent = viewModel::onSignOutDialogEvent,
        accountDeletionDialogState = accountDeletionDialogState,
        onAccountDeletionDialogEvent = viewModel::onAccountDeletionDialogEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    topBarState: ProfileDetailsTopBarState,
    onTopBarEvent: (ProfileDetailsTopBarEvent) -> Unit,
    profileDetailsState: ProfileDetailsState,
    onProfileDetailsEvent: (ProfileDetailsEvent) -> Unit,
    signOutDialogState: SignOutDialogState,
    onSignOutDialogEvent: (SignOutDialogEvent) -> Unit,
    accountDeletionDialogState: AccountDeletionDialogState,
    onAccountDeletionDialogEvent: (AccountDeletionDialogEvent) -> Unit,
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<ProfileDetailsSideEffect>,
    navActions: ProfileDetailsNavActions,
) {
    ProfileDetailsScreenBehavior(
        onLifecycleEvent = onLifecycleEvent,
        sideEffects = sideEffects,
        navActions = navActions,
    )

    var isDatePickerVisible by remember { mutableStateOf(false) }
    if (isDatePickerVisible) {
        val birthDateEpochMillis =
            (profileDetailsState as? ProfileDetailsState.Success)?.birthDateEpochMillis
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = birthDateEpochMillis,
            yearRange = remember { User.BIRTH_DATE_MIN_VALUE.year..LocalDate.now().year }
        )

        ZarinaDatePickerDialog(
            onDismissRequest = { isDatePickerVisible = false },
            datePickerState = datePickerState,
            onDateSelected = {
                onProfileDetailsEvent(ProfileDetailsEvent.BirthDateEpochMillisChanged(it))
                isDatePickerVisible = false
            },
        )
    }

    if (signOutDialogState is SignOutDialogState.Visible) {
        SignOutDialog(
            state = signOutDialogState,
            onEvent = onSignOutDialogEvent,
        )
    }

    if (accountDeletionDialogState is AccountDeletionDialogState.Visible) {
        AccountDeletionDialog(
            state = accountDeletionDialogState,
            onEvent = onAccountDeletionDialogEvent,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        ProfileDetailsTopBar(
            state = topBarState,
            onEvent = onTopBarEvent,
        )

        ProfileDetailsContent(
            state = profileDetailsState,
            onEvent = onProfileDetailsEvent,
            onBirthDateClicked = { isDatePickerVisible = true },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
