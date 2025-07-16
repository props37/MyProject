package ru.livetyping.zarina.data.location

import ru.livetyping.zarina.core.domain.model.common.Location

internal interface LocationDataSource {
    suspend fun getCurrentLocation(): Location?
}
