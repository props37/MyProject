package ru.livetyping.zarina.util.library.coroutines

import kotlinx.coroutines.flow.SharingStarted

private const val AndroidUiSubscriptionDelayMillis = 5000L

/**
 * UI specific version of [SharingStarted.WhileSubscribed] that is used to not cancel
 * upstream flows if the UI stopped listening for a fraction of a second, for example
 * when the user rotates the device and the view is destroyed and recreated in quick succession.
 *
 * See [Migrating from LiveData to Kotlin’s Flow](https://medium.com/androiddevelopers/migrating-from-livedata-to-kotlins-flow-379292f419fb)
 * Medium article for more details
 */
val SharingStarted.Companion.WhileUiSubscribed: SharingStarted
    get() = WhileSubscribed(AndroidUiSubscriptionDelayMillis)
