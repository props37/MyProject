package ru.livetyping.zarina.feature.profile.ui

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.navigation.ScreenResult

public data class ProfileSelectedCityResult(
    override val id: String,
    val city: City,
) : ScreenResult
