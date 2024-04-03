package ru.livetyping.zarina.data.old.user.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.City

interface IUserLocalSource {

    suspend fun setCity(city: City?)

    fun getCity(): Flow<City?>

}

