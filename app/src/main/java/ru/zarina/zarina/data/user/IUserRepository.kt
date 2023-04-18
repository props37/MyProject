package ru.zarina.zarina.data.user

import ru.zarina.zarina.domain.City

interface IUserRepository {

    suspend fun setCity(city: City)

}
