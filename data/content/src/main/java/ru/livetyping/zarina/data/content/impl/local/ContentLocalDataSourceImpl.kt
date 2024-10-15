package ru.livetyping.zarina.data.content.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.data.content.impl.local.gender.ContentGenderDataHolder
import javax.inject.Inject

internal class ContentLocalDataSourceImpl @Inject constructor(
    private val contentGenderDataHolder: ContentGenderDataHolder,
) : ContentLocalDataSource {
    override fun getLastContentGenderFlow(): Flow<Gender?> {
        return contentGenderDataHolder.getLastContentGenderFlow()
    }

    override suspend fun setLastContentGender(gender: Gender) {
        contentGenderDataHolder.setLastContentGender(gender)
    }
}
