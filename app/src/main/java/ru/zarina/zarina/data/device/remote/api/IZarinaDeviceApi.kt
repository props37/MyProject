package ru.zarina.zarina.data.device.remote.api

import ru.zarina.zarina.data.device.remote.api.dto.TokenDto

interface IZarinaDeviceApi {
    suspend fun getToken(): TokenDto
}
