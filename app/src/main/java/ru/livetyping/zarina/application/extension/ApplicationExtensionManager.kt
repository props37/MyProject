package ru.livetyping.zarina.application.extension

import javax.inject.Inject

class ApplicationExtensionManager @Inject constructor(
    timber: TimberApplicationExtension,
    mindbox: MindboxApplicationExtension,
    coil: CoilApplicationExtension,
    userFetcher: UserFetcherApplicationExtension,
    userCityFetcher: UserCityFetcherApplicationExtension,
    authorizationTokenFetcher: AuthorizationTokenFetcherApplicationExtension,
    favoriteProductIdsFetcher: FavoriteProductIdsFetcherApplicationExtension,
    cartProductIdsFetcher: CartProductIdsFetcherApplicationExtension,
    appMetricaApplicationExtension: AppMetricaApplicationExtension,
) {
    val extensions = listOf(
        timber,
        mindbox,
        coil,
        userFetcher,
        userCityFetcher,
        authorizationTokenFetcher,
        favoriteProductIdsFetcher,
        cartProductIdsFetcher,
        appMetricaApplicationExtension,
    )
}
