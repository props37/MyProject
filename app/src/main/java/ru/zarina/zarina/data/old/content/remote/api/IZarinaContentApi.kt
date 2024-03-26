package ru.zarina.zarina.data.old.content.remote.api

import ru.zarina.zarina.data.old.content.remote.api.dto.BannerDto
import ru.zarina.zarina.data.old.content.remote.api.dto.SelectionDto
import ru.zarina.zarina.data.old.content.remote.api.dto.SplashDto

interface IZarinaContentApi {
    suspend fun getOnboardingSplash(): SplashDto
    suspend fun getBanners(): List<BannerDto>
    suspend fun getSelections(): List<SelectionDto>
}
