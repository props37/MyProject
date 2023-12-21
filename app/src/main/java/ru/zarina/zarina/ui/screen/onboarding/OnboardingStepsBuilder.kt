package ru.zarina.zarina.ui.screen.onboarding

import android.Manifest
import android.os.Build
import ru.zarina.zarina.data.permissionmanager.PermissionManager
import ru.zarina.zarina.data.permissionmanager.isDenied
import ru.zarina.zarina.domain.rework.OnboardingStep

// TODO: [High] Refactor
object OnboardingStepsBuilder {
    fun build(permissionManager: PermissionManager): List<OnboardingStep> {
        return buildList {
            OnboardingStep.entries.forEach { step ->
                when (step) {
                    OnboardingStep.NOTIFICATIONS_SETUP -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val notificationsPermissionState =
                                permissionManager.getPermissionState(Manifest.permission.POST_NOTIFICATIONS)
                            if (notificationsPermissionState.isDenied) {
                                add(step)
                            }
                        }
                    }

                    OnboardingStep.CITY_DETECTION -> add(step)
                    OnboardingStep.CITY_CONFIRMATION -> add(step)
                }
            }
        }
    }
}
