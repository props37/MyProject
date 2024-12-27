package ru.livetyping.zarina.data.store.impl.remote.api

import ru.livetyping.zarina.data.store.impl.remote.api.dto.StoresDto

internal interface StoreApi {
    suspend fun getStores(): List<StoresDto>
}
