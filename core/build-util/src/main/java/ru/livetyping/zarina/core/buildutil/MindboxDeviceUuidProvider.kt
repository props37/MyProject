package ru.livetyping.zarina.core.buildutil

import kotlinx.coroutines.flow.Flow

public interface MindboxDeviceUuidProvider {
    public fun getMindboxDeviceUuidFlow(): Flow<String?>
}
