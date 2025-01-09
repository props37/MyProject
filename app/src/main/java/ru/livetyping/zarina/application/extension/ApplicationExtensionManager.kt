package ru.livetyping.zarina.application.extension

import javax.inject.Inject

class ApplicationExtensionManager @Inject constructor(
    timber: TimberApplicationExtension,
    mindbox: MindboxApplicationExtension,
    coil: CoilApplicationExtension,
    userFetcher: UserFetcherApplicationExtension,
    userCityFetcher: UserCityFetcherApplicationExtension,
    bearerTokenFetcher: BearerTokenFetcherApplicationExtension,
    favoriteProductIdsFetcher: FavoriteProductIdsFetcherApplicationExtension,
    cartProductIdsFetcher: CartProductIdsFetcherApplicationExtension,
) {
    val extensions = listOf(
        timber,
        mindbox,
        coil,
        userFetcher,
        userCityFetcher,
        bearerTokenFetcher,
        favoriteProductIdsFetcher,
        cartProductIdsFetcher,
    )
}
