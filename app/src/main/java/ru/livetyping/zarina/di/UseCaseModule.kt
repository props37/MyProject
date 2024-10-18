package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.usecase.auth.FetchUnauthorizedUserBearerTokensUseCase
import ru.livetyping.zarina.core.domain.usecase.auth.GetBearerTokensFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.auth.RefreshBearerTokensUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.GetLastContentGenderFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository
import ru.livetyping.zarina.feature.home.domain.usecase.GetHomeContentFlowUseCase

@Module
@InstallIn(SingletonComponent::class)
internal class UseCaseModule {

    @Provides
    fun provideFetchUnauthorizedUserBearerTokensUseCase(
        authRepository: AuthRepository,
        logger: UseCaseLogger,
    ): FetchUnauthorizedUserBearerTokensUseCase {
        return FetchUnauthorizedUserBearerTokensUseCase.getInstance(
            authRepository = authRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetBearerTokensFlowUseCase(
        authRepository: AuthRepository,
        logger: UseCaseLogger,
    ): GetBearerTokensFlowUseCase {
        return GetBearerTokensFlowUseCase.getInstance(
            authRepository = authRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideRefreshBearerTokensUseCase(
        authRepository: AuthRepository,
        logger: UseCaseLogger,
    ): RefreshBearerTokensUseCase {
        return RefreshBearerTokensUseCase.getInstance(
            authRepository = authRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetLastContentGenderFlowUseCase(
        contentRepository: ContentRepository,
        logger: UseCaseLogger,
    ): GetLastContentGenderFlowUseCase {
        return GetLastContentGenderFlowUseCase.getInstance(
            contentRepository = contentRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSetLastContentGenderUseCase(
        contentRepository: ContentRepository,
        logger: UseCaseLogger,
    ): SetLastContentGenderUseCase {
        return SetLastContentGenderUseCase.getInstance(
            contentRepository = contentRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetHomeContentFlowUseCase(
        homeContentRepository: HomeContentRepository,
        logger: UseCaseLogger,
    ): GetHomeContentFlowUseCase {
        return GetHomeContentFlowUseCase.getInstance(
            homeContentRepository = homeContentRepository,
            logger = logger,
        )
    }
}
