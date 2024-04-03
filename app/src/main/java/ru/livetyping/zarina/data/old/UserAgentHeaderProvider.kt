package ru.livetyping.zarina.data.old

import android.os.Build
import org.koin.core.annotation.Singleton
import ru.livetyping.zarina.BuildConfig

@Singleton
class UserAgentHeaderProvider {

    private val headers = mutableMapOf(
        getUserAgentHeader(),
        getClientVersionHeader(),
        getClientSourceHeader(),
    )

    fun getHeaders(): Map<String, String> = headers

    private fun getUserAgentHeader(): Pair<String, String> {
        val appVersion = BuildConfig.VERSION_NAME
        val systemVersion = Build.VERSION.RELEASE
        return KEY_USER_AGENT to "Zarina/$appVersion (Android $systemVersion)"
    }

    private fun getClientVersionHeader(): Pair<String, String> {
        return KEY_CLIENT_VERSION to BuildConfig.VERSION_NAME
    }

    // This header is used to differentiate between clients (android, ios, etc) on backend.
    private fun getClientSourceHeader(): Pair<String, String> {
        return KEY_CLIENT_SOURCE to VALUE_CLIENT_SOURCE
    }

    companion object {
        private const val KEY_USER_AGENT = "user-agent"
        private const val KEY_CLIENT_VERSION = "x-client-version"
        private const val KEY_CLIENT_SOURCE = "x-client-source"
        private const val VALUE_CLIENT_SOURCE = "phoh8Meimu1uQuoH"
    }

}
