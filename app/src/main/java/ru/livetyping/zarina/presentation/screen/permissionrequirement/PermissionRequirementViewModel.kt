package ru.livetyping.zarina.presentation.screen.permissionrequirement

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.packagename.PackageName
import ru.livetyping.zarina.presentation.common.systemsettings.SystemSettings
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.permissionrequirement.PermissionRequirementViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class PermissionRequirementViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val permission: StateFlow<UnscopedDestinations.PermissionRequirement.Permission> =
        savedStateHandle
            .getStateFlow<UnscopedDestinations.PermissionRequirement.Permission?>(
                key = UnscopedDestinations.PermissionRequirement.ARG_KEY_PERMISSION,
                initialValue = null,
            )
            .mapState(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
            ) { permission ->
                checkNotNull(permission) { "permission is null" }
            }

    val title: StateFlow<Text> = savedStateHandle
        .getStateFlow<Text?>(
            key = UnscopedDestinations.PermissionRequirement.ARG_KEY_TITLE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { title ->
            checkNotNull(title) { "title is null" }
        }

    val body: StateFlow<Text> = savedStateHandle
        .getStateFlow<Text?>(
            key = UnscopedDestinations.PermissionRequirement.ARG_KEY_BODY,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { body ->
            checkNotNull(body) { "body is null" }
        }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = PermissionRequirementScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onGoToSettingsClicked() {
        navigationThrottler.throttle {
            val settings = getSystemSettingsFromPermission(permission.value)
            emitSideEffect(SideEffect.OpenSystemSettings(settings))

            val action = PermissionRequirementScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun getSystemSettingsFromPermission(
        permission: UnscopedDestinations.PermissionRequirement.Permission,
    ) : SystemSettings {
        return when (permission) {
            UnscopedDestinations.PermissionRequirement.Permission.LOCATION -> {
                SystemSettings.ApplicationDetails(PackageName.Own)
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: PermissionRequirementScreenAction) : SideEffect

        data class OpenSystemSettings(val settings: SystemSettings) : SideEffect
    }
}
