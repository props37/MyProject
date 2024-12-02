package ru.livetyping.zarina.data.content.impl.local.gender

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.gender.Gender

internal interface ContentGenderDataHolder {
    fun getLastContentGenderFlow(): Flow<Gender?>

    suspend fun setLastContentGender(gender: Gender)

    suspend fun clear()
}
