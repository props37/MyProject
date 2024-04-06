package ru.livetyping.zarina.data.common.remote.api.zarina

import ru.livetyping.zarina.data.common.remote.api.headerprovider.HeaderProvider
import javax.inject.Inject

class ZarinaApiHeaderProvider @Inject constructor() : HeaderProvider {
    override fun provide(): Map<String, String> {
        return mapOf(getClientSourceHeader())
    }

    private fun getClientSourceHeader(): Pair<String, String> {
        return KEY_CLIENT_SOURCE to VALUE_CLIENT_SOURCE
    }

    companion object {
        private const val KEY_CLIENT_SOURCE = "x-client-source"

        private const val VALUE_CLIENT_SOURCE = "android-app"
    }
}
