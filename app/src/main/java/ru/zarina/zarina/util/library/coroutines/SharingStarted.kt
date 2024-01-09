package ru.zarina.zarina.util.library.coroutines

import kotlinx.coroutines.flow.SharingStarted

/**
 * Android-specific delay for [SharingStarted.WhileSubscribed] that is used to not cancel
 * upstream flows if the view stopped listening for a fraction of a second, for example
 * when the user rotates the device and the view is destroyed and recreated in quick succession.
 *
 * See [Migrating from LiveData to Kotlin’s Flow](https://medium.com/androiddevelopers/migrating-from-livedata-to-kotlins-flow-379292f419fb)
 * Medium article for more details
 */
val SharingStarted.Companion.WhileSubscribedDelay: Long
    get() = 5000L
