package ru.zarina.zarina.data.old

import org.koin.core.annotation.Singleton
import ru.zarina.zarina.BuildConfig

@Singleton
class MindboxHeaderProvider {

    private val headers = mutableMapOf(
        getAuthorizationHeader(),
    )

    fun getHeaders(): Map<String, String> = headers

    private fun getAuthorizationHeader(): Pair<String, String> {
        val value = "Mindbox secretKey=\"${BuildConfig.MINDBOX_KEY}\""
        return KEY_AUTHORIZATION to value
    }

    companion object {
        private const val KEY_AUTHORIZATION = "Authorization"
    }

}
