package ru.zarina.zarina.data.old.device.remote.api

import ru.zarina.zarina.data.old.device.remote.api.dto.TokenDto

interface IZarinaDeviceApi {
    suspend fun getToken(): TokenDto
}
