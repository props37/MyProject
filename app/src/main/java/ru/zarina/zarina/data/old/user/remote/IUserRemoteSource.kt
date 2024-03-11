package ru.zarina.zarina.data.old.user.remote

import ru.zarina.zarina.domain.City

interface IUserRemoteSource {

    suspend fun setCity(city: City)

}
