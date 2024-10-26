package ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal enum class OnboardingStep : Parcelable {
    NOTIFICATIONS_SETUP,
    CITY_DETECTION,
    CITY_CONFIRMATION,
}
