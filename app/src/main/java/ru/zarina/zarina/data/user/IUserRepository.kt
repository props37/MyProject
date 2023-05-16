package ru.zarina.zarina.data.user

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.City

interface IUserRepository {

    suspend fun setCity(city: City)

    fun getCity(): Flow<City?>

}
