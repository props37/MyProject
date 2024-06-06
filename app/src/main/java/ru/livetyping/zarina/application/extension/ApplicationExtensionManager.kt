package ru.livetyping.zarina.application.extension

import javax.inject.Inject

class ApplicationExtensionManager @Inject constructor(
    timber: TimberApplicationExtension,
    mindbox: MindboxApplicationExtension,
    coil: CoilApplicationExtension,
    recaptcha: RecaptchaApplicationExtension,
    userFetcher: UserFetcherApplicationExtension,
    authorizationTokenFetcher: AuthorizationTokenFetcherApplicationExtension,
    favoriteProductIdsFetcher: FavoriteProductIdsFetcherApplicationExtension,
    cartProductIdsFetcher: CartProductIdsFetcherApplicationExtension,
) {
    val extensions = listOf(
        timber,
        mindbox,
        coil,
        recaptcha,
        userFetcher,
        authorizationTokenFetcher,
        favoriteProductIdsFetcher,
        cartProductIdsFetcher,
    )
}
