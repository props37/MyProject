package ru.livetyping.zarina.presentation.app

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.manager.ForcedSignOutCoordinator
import ru.livetyping.zarina.core.domain.usecase.auth.GetBearerTokensFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductCountFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.onboarding.GetIsOnboardingCompletedFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.ForcedSignOutUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetInAppReviewRequestFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.googleplayservices.review.InAppReviewManager
import javax.inject.Inject

@OptIn(UnstableApi::class)
class AppDependencies @Inject constructor(
    val getIsOnboardingCompletedFlow: GetIsOnboardingCompletedFlowUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val getCartProductCountFlow: GetCartProductCountFlowUseCase,
    val forcedSignOutCoordinator: ForcedSignOutCoordinator,
    val forcedSignOut: ForcedSignOutUseCase,
    val exoPlayerCacheDataSourceFactory: CacheDataSource.Factory,
    val appMetrica: AppMetrica,
    val getBearerTokensFlow: GetBearerTokensFlowUseCase,
    val getUserFlow: GetUserFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val inAppReviewManager: InAppReviewManager,
    val getInAppReviewRequestFlow: GetInAppReviewRequestFlowUseCase,
)
