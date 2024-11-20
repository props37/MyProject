package ru.livetyping.zarina.data.user.impl.local.city

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City

internal interface UserCityDataHolder {
    fun getUserCityFlow(): Flow<City?>

    suspend fun setUserCity(city: City?)
}
