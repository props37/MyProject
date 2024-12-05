package ru.livetyping.zarina.feature.onboarding.ui

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.navigation.ScreenResult

public data class OnboardingSelectedCityResult(
    override val id: String,
    val city: City,
) : ScreenResult
