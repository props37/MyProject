package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location

public interface LocationRepository {
    public fun getCurrentLocationFlow(): Flow<Location?>
}
