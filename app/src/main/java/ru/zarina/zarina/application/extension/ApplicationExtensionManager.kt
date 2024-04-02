package ru.zarina.zarina.application.extension

import javax.inject.Inject

class ApplicationExtensionManager @Inject constructor(
    timber: TimberApplicationExtension,
    mindbox: MindboxApplicationExtension,
    coil: CoilApplicationExtension,
    recaptcha: RecaptchaApplicationExtension,
    authorizationTokenFetcher: AuthorizationTokenFetcherApplicationExtension,
    favoriteProductIdsFetcher: FavoriteProductIdsFetcherApplicationExtension,
    cartProductIdsFetcher: CartProductIdsFetcherApplicationExtension,
) {
    val extensions = listOf(
        timber,
        mindbox,
        coil,
        recaptcha,
        authorizationTokenFetcher,
        favoriteProductIdsFetcher,
        cartProductIdsFetcher,
    )
}
