package ru.zarina.zarina.data

import ru.zarina.zarina.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MindboxHeaderProvider @Inject constructor() {

    private val headers = mutableMapOf(
        getAuthorizationHeader(),
    )

    fun getHeaders(): Map<String, String> = headers

    private fun getAuthorizationHeader(): Pair<String, String> {
        val value = "Mindbox secretKey=\"${BuildConfig.MINDBOX_SECRET}\""
        return KEY_AUTHORIZATION to value
    }

    companion object {
        private const val KEY_AUTHORIZATION = "Authorization"
    }

}
