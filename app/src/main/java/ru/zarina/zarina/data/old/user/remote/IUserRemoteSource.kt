package ru.zarina.zarina.data.old.user.remote

import ru.zarina.zarina.domain.old.City

interface IUserRemoteSource {

    suspend fun setCity(city: City)

}
