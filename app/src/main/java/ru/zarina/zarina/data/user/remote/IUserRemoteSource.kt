package ru.zarina.zarina.data.user.remote

import ru.zarina.zarina.domain.City

interface IUserRemoteSource {

    suspend fun setCity(city: City)

}
