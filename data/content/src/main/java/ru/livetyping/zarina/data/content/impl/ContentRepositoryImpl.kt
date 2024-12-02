package ru.livetyping.zarina.data.content.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.data.content.impl.local.ContentLocalDataSource
import javax.inject.Inject

internal class ContentRepositoryImpl @Inject constructor(
    private val localDataSource: ContentLocalDataSource,
) : ContentRepository {
    override fun getLastContentGenderFlow(): Flow<Gender?> {
        return localDataSource.getLastContentGenderFlow()
    }

    override suspend fun setLastContentGender(gender: Gender) {
        localDataSource.setLastContentGender(gender)
    }

    override suspend fun clear() {
        localDataSource.clear()
    }
}
