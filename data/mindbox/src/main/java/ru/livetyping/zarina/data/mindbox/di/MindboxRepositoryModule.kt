package ru.livetyping.zarina.data.mindbox.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.MindboxRepository
import ru.livetyping.zarina.data.mindbox.impl.MindboxRepositoryImpl
import ru.livetyping.zarina.data.mindbox.impl.api.MindboxApi
import ru.livetyping.zarina.data.mindbox.impl.api.MindboxApiImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MindboxRepositoryModule {

    @Binds
    abstract fun bindMindboxRepository(impl: MindboxRepositoryImpl): MindboxRepository

    @Binds
    abstract fun bindMindboxApi(impl: MindboxApiImpl): MindboxApi
}
