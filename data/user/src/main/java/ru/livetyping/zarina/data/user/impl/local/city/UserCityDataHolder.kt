package ru.livetyping.zarina.data.user.impl.local.city

import ru.livetyping.zarina.core.domain.model.geo.City

internal interface UserCityDataHolder {
    suspend fun setUserCity(city: City?)
}
