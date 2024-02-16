package ru.zarina.zarina.application.extension

import javax.inject.Inject

class ApplicationExtensionManager @Inject constructor(
    timber: TimberApplicationExtension,
    mindbox: MindboxApplicationExtension,
    coil: CoilApplicationExtension,
    authorizationTokenFetcher: AuthorizationTokenFetcherApplicationExtension,
) {
    val extensions = listOf(timber, mindbox, coil, authorizationTokenFetcher)
}
