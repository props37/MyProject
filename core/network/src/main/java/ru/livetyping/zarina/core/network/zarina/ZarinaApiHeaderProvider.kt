package ru.livetyping.zarina.core.network.zarina

internal class ZarinaApiHeaderProvider(private val appVersionCode: Int) {
    fun provide(): Map<String, String> {
        return mapOf(
            KEY_CLIENT_SOURCE to VALUE_CLIENT_SOURCE,
            KEY_CLIENT_VERSION to appVersionCode.toString(),
        )
    }

    companion object {
        private const val KEY_CLIENT_SOURCE = "x-client-source"
        private const val KEY_CLIENT_VERSION = "x-client-version"

        private const val VALUE_CLIENT_SOURCE = "android-app"
    }
}
