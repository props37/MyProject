package ru.zarina.zarina.data.user.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.City

interface IUserLocalSource {

    suspend fun setCity(city: City?)

    fun getCity(): Flow<City?>

}

