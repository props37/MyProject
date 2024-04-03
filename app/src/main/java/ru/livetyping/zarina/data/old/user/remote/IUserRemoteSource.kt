package ru.livetyping.zarina.data.old.user.remote

import ru.livetyping.zarina.domain.old.City

interface IUserRemoteSource {

    suspend fun setCity(city: City)

}
