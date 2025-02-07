package ru.livetyping.zarina.application.extension

import javax.inject.Inject

class ApplicationExtensionManager @Inject constructor(
    userFetcher: UserFetcherApplicationExtension,
    userCityFetcher: UserCityFetcherApplicationExtension,
    favoriteProductIdsFetcher: FavoriteProductIdsFetcherApplicationExtension,
    cartProductIdsFetcher: CartProductIdsFetcherApplicationExtension,
    appMetricaApplicationExtension: AppMetricaApplicationExtension,
) {
    val extensions = listOf(
        userFetcher,
        userCityFetcher,
        favoriteProductIdsFetcher,
        cartProductIdsFetcher,
        appMetricaApplicationExtension,
    )
}
