package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.buildutil.BuildType

@Module
@InstallIn(SingletonComponent::class)
internal class BuildTypeModule {

    @Provides
    fun provideBuildType(): BuildType {
        return when (val buildTypeString = BuildConfig.BUILD_TYPE) {
            DEBUG -> BuildType.DEBUG
            QA -> BuildType.QA
            RELEASE -> BuildType.RELEASE
            else -> error("Unknown build type $buildTypeString")
        }
    }

    private companion object {
        private const val DEBUG = "debug"
        private const val QA = "qa"
        private const val RELEASE = "release"
    }
}
