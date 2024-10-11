package ru.livetyping.zarina.core.network.impl

import javax.inject.Inject

internal class ZarinaApiHeaderProvider @Inject constructor() {
    fun provide(): Map<String, String> {
        return mapOf(KEY_CLIENT_SOURCE to VALUE_CLIENT_SOURCE)
    }

    companion object {
        private const val KEY_CLIENT_SOURCE = "x-client-source"

        private const val VALUE_CLIENT_SOURCE = "android-app"
    }
}
