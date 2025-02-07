package ru.livetyping.zarina.application.extension

import javax.inject.Inject

class ApplicationExtensionManager @Inject constructor(
    userFetcher: UserFetcherApplicationExtension,
    userCityFetcher: UserCityFetcherApplicationExtension,
    favoriteProductIdsFetcher: FavoriteProductIdsFetcherApplicationExtension,
    cartProductIdsFetcher: CartProductIdsFetcherApplicationExtension,
) {
    val extensions = listOf(
        userFetcher,
        userCityFetcher,
        favoriteProductIdsFetcher,
        cartProductIdsFetcher,
    )
}
