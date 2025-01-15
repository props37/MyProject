package ru.livetyping.zarina.feature.cart.ui.api

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.navigation.ScreenResult

public data class CartSelectedCityResult(
    override val id: String,
    val city: City,
) : ScreenResult
