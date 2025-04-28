package ru.livetyping.zarina.data.content.impl.local

import javax.inject.Inject

internal class ContentLocalDataSourceImpl @Inject constructor() : ContentLocalDataSource {
    override suspend fun clear() {}
}
