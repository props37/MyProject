package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.gender.Gender

public interface ContentRepository {
    public fun getLastContentGenderFlow(): Flow<Gender?>

    public suspend fun setLastContentGender(gender: Gender)

    public suspend fun clear()
}
