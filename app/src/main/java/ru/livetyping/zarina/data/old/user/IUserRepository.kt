package ru.livetyping.zarina.data.old.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.City

interface IUserRepository {

    suspend fun setCity(city: City)

    fun getCity(): Flow<City?>

}
