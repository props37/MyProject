package ru.livetyping.zarina.data.content.impl

import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.data.content.impl.local.ContentLocalDataSource
import javax.inject.Inject

internal class ContentRepositoryImpl @Inject constructor(
    private val localDataSource: ContentLocalDataSource,
) : ContentRepository {
    override suspend fun clear() {
        localDataSource.clear()
    }
}
