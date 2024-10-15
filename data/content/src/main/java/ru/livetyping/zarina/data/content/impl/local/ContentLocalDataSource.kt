package ru.livetyping.zarina.data.content.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.gender.Gender

internal interface ContentLocalDataSource {
    fun getLastContentGenderFlow(): Flow<Gender?>

    suspend fun setLastContentGender(gender: Gender)
}
