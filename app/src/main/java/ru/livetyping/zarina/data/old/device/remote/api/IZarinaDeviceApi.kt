package ru.livetyping.zarina.data.old.device.remote.api

import ru.livetyping.zarina.data.old.device.remote.api.dto.TokenDto

interface IZarinaDeviceApi {
    suspend fun getToken(): TokenDto
}
