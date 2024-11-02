package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.repository.LocationRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.auth.FetchUnauthorizedUserBearerTokensUseCase
import ru.livetyping.zarina.core.domain.usecase.auth.GetBearerTokensFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.auth.RefreshBearerTokensUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoriesFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.GetLastContentGenderFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase
import ru.livetyping.zarina.core.domain.usecase.location.GetCurrentLocationFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ClearWishlistUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.FetchWishlistProductIdsUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductPageFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
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

    @Provides
    fun provideGetCategoriesFlowUseCase(
        categoryRepository: CategoryRepository,
        logger: UseCaseLogger,
    ): GetCategoriesFlowUseCase {
        return GetCategoriesFlowUseCase.getInstance(
            categoryRepository = categoryRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideClearWishlistUseCase(
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): ClearWishlistUseCase {
        return ClearWishlistUseCase.getInstance(
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideFetchWishlistProductIdsUseCase(
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): FetchWishlistProductIdsUseCase {
        return FetchWishlistProductIdsUseCase.getInstance(
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetWishlistProductIdsFlowUseCase(
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): GetWishlistProductIdsFlowUseCase {
        return GetWishlistProductIdsFlowUseCase.getInstance(
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetWishlistProductPageFlowUseCase(
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): GetWishlistProductPageFlowUseCase {
        return GetWishlistProductPageFlowUseCase.getInstance(
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideToggleProductInWishlistUseCase(
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): ToggleProductInWishlistUseCase {
        return ToggleProductInWishlistUseCase.getInstance(
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetCurrentLocationFlowUseCase(
        locationRepository: LocationRepository,
        logger: UseCaseLogger,
    ): GetCurrentLocationFlowUseCase {
        return GetCurrentLocationFlowUseCase.getInstance(
            locationRepository = locationRepository,
            logger = logger,
        )
    }
}
