package ru.zarina.zarina.data.content

import ru.zarina.zarina.domain.Url

interface IContentRepository {
    suspend fun getOnboardingSplash(): Url?
}
