package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.manager.ForcedSignOutCoordinator
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.domain.repository.LocationRepository
import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.auth.FetchUnauthorizedUserBearerTokensUseCase
import ru.livetyping.zarina.core.domain.usecase.auth.GetBearerTokensFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.auth.RefreshBearerTokensUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.RemoveProductFromCartUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoriesFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.GetLastContentGenderFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase
import ru.livetyping.zarina.core.domain.usecase.geo.GetCitiesFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.geo.GetCurrentCityByLocationFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.location.GetCurrentLocationFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.onboarding.GetOnboardingBannerUrlFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.onboarding.SetIsOnboardingCompletedUseCase
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderPageFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductsWithFiltersPageFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.SubscribeToProductUseCase
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignUpUseCase
import ru.livetyping.zarina.core.domain.usecase.user.DeleteAccountUseCase
import ru.livetyping.zarina.core.domain.usecase.user.ForcedSignOutUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetForcedSignOutRequestsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyCardFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetYandexCaptchaUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestPasswordResetUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetLocalUserCityUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByEmailUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByPhoneUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignOutUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignUpUseCase
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserNotificationsSettingsUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ClearWishlistUseCase
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
        forcedSignOutCoordinator: ForcedSignOutCoordinator,
        logger: UseCaseLogger,
    ): RefreshBearerTokensUseCase {
        return RefreshBearerTokensUseCase.getInstance(
            authRepository = authRepository,
            forcedSignOutCoordinator = forcedSignOutCoordinator,
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

    @Provides
    fun provideGetOnboardingBannerUrlFlowUseCase(
        onboardingRepository: OnboardingRepository,
        logger: UseCaseLogger,
    ): GetOnboardingBannerUrlFlowUseCase {
        return GetOnboardingBannerUrlFlowUseCase.getInstance(
            onboardingRepository = onboardingRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetCurrentCityByLocationFlowUseCase(
        locationRepository: LocationRepository,
        geographyRepository: GeographyRepository,
        logger: UseCaseLogger,
    ): GetCurrentCityByLocationFlowUseCase {
        return GetCurrentCityByLocationFlowUseCase.getInstance(
            locationRepository = locationRepository,
            geographyRepository = geographyRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSetIsOnboardingCompletedUseCase(
        onboardingRepository: OnboardingRepository,
        logger: UseCaseLogger,
    ): SetIsOnboardingCompletedUseCase {
        return SetIsOnboardingCompletedUseCase.getInstance(
            onboardingRepository = onboardingRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSetUserCityUseCase(
        userRepository: UserRepository,
        cartRepository: CartRepository,
        logger: UseCaseLogger,
    ): SetUserCityUseCase {
        return SetUserCityUseCase.getInstance(
            userRepository = userRepository,
            cartRepository = cartRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSetLocalUserCityUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): SetLocalUserCityUseCase {
        return SetLocalUserCityUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetCitiesFlowUseCase(
        geographyRepository: GeographyRepository,
        logger: UseCaseLogger,
    ): GetCitiesFlowUseCase {
        return GetCitiesFlowUseCase.getInstance(
            geographyRepository = geographyRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetUserFlowUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): GetUserFlowUseCase {
        return GetUserFlowUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetLoyaltyCardFlowUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): GetLoyaltyCardFlowUseCase {
        return GetLoyaltyCardFlowUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetUserCityFlowUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): GetUserCityFlowUseCase {
        return GetUserCityFlowUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSignInByEmailUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
        logger: UseCaseLogger,
    ): SignInByEmailUseCase {
        return SignInByEmailUseCase.getInstance(
            userRepository = userRepository,
            authRepository = authRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSignInByPhoneUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): SignInByPhoneUseCase {
        return SignInByPhoneUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetYandexCaptchaUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): GetYandexCaptchaUseCase {
        return GetYandexCaptchaUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetOrderPageFlowUseCase(
        orderRepository: OrderRepository,
        logger: UseCaseLogger,
    ): GetOrderPageFlowUseCase {
        return GetOrderPageFlowUseCase.getInstance(
            orderRepository = orderRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSignUpUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): SignUpUseCase {
        return SignUpUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideRequestPasswordResetUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): RequestPasswordResetUseCase {
        return RequestPasswordResetUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetForcedSignOutRequestsFlowUseCase(
        forcedSignOutCoordinator: ForcedSignOutCoordinator,
        logger: UseCaseLogger,
    ): GetForcedSignOutRequestsFlowUseCase {
        return GetForcedSignOutRequestsFlowUseCase.getInstance(
            forcedSignOutCoordinator = forcedSignOutCoordinator,
            logger = logger,
        )
    }

    @Provides
    fun provideForcedSignOutUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        contentRepository: ContentRepository,
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): ForcedSignOutUseCase {
        return ForcedSignOutUseCase.getInstance(
            authRepository = authRepository,
            userRepository = userRepository,
            contentRepository = contentRepository,
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideUpdateUserNotificationsSettingsUseCase(
        userRepository: UserRepository,
        logger: UseCaseLogger,
    ): UpdateUserNotificationsSettingsUseCase {
        return UpdateUserNotificationsSettingsUseCase.getInstance(
            userRepository = userRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSignOutUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
        contentRepository: ContentRepository,
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): SignOutUseCase {
        return SignOutUseCase.getInstance(
            userRepository = userRepository,
            authRepository = authRepository,
            contentRepository = contentRepository,
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideDeleteAccountUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
        contentRepository: ContentRepository,
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): DeleteAccountUseCase {
        return DeleteAccountUseCase.getInstance(
            userRepository = userRepository,
            authRepository = authRepository,
            contentRepository = contentRepository,
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetCategoryFlowUseCase(
        categoryRepository: CategoryRepository,
        logger: UseCaseLogger,
    ): GetCategoryFlowUseCase {
        return GetCategoryFlowUseCase.getInstance(
            categoryRepository = categoryRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetProductsWithFiltersPageFlowUseCase(
        productRepository: ProductRepository,
        logger: UseCaseLogger,
    ): GetProductsWithFiltersPageFlowUseCase {
        return GetProductsWithFiltersPageFlowUseCase.getInstance(
            productRepository = productRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetCartProductIdsFlowUseCase(
        cartRepository: CartRepository,
        logger: UseCaseLogger,
    ): GetCartProductIdsFlowUseCase {
        return GetCartProductIdsFlowUseCase.getInstance(
            cartRepository = cartRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideAddProductToCartUseCase(
        cartRepository: CartRepository,
        logger: UseCaseLogger,
    ): AddProductToCartUseCase {
        return AddProductToCartUseCase.getInstance(
            cartRepository = cartRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideRemoveProductFromCartUseCase(
        cartRepository: CartRepository,
        logger: UseCaseLogger,
    ): RemoveProductFromCartUseCase {
        return RemoveProductFromCartUseCase.getInstance(
            cartRepository = cartRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideSubscribeToProductUseCase(
        productRepository: ProductRepository,
        logger: UseCaseLogger,
    ): SubscribeToProductUseCase {
        return SubscribeToProductUseCase.getInstance(
            productRepository = productRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetProductFlowUseCase(
        productRepository: ProductRepository,
        cartRepository: CartRepository,
        wishlistRepository: WishlistRepository,
        logger: UseCaseLogger,
    ): GetProductFlowUseCase {
        return GetProductFlowUseCase.getInstance(
            productRepository = productRepository,
            cartRepository = cartRepository,
            wishlistRepository = wishlistRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideGetOrderFlowUseCase(
        orderRepository: OrderRepository,
        logger: UseCaseLogger,
    ): GetOrderFlowUseCase {
        return GetOrderFlowUseCase.getInstance(
            orderRepository = orderRepository,
            logger = logger,
        )
    }

    @Provides
    fun provideConfirmSignUpUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
        logger: UseCaseLogger,
    ): ConfirmSignUpUseCase {
        return ConfirmSignUpUseCase.getInstance(
            userRepository = userRepository,
            authRepository = authRepository,
            logger = logger,
        )
    }
}
