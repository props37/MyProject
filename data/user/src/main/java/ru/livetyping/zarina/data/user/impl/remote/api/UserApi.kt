package ru.livetyping.zarina.data.user.impl.remote.api

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.network.zarina.dto.CityDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.AuthDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.GetLoyaltyCardDto
import ru.livetyping.zarina.data.user.impl.remote.api.dto.UserDto

internal interface UserApi {
    suspend fun getUser(): UserDto

    suspend fun getUserCity(): CityDto

    suspend fun setUserCity(city: City)

    suspend fun getLoyaltyCard(): GetLoyaltyCardDto

    // TODO: [Top] Add yandex captcha token
    suspend fun signIn(email: Email, password: String): AuthDto

    // TODO: [Top] Add yandex captcha token
    suspend fun signIn(phone: PhoneNumber)
}
