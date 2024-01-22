package ru.zarina.zarina.data.rework.common.remote.headerprovider

import javax.inject.Inject

// TODO: [Low] Add User-Agent header?

class ZarinaApiHeaderProvider @Inject constructor() : HeaderProvider {
    override fun provide(): Map<String, String> {
        return mapOf(getClientSourceHeader())
    }

    private fun getClientSourceHeader(): Pair<String, String> {
        return KEY_CLIENT_SOURCE to VALUE_CLIENT_SOURCE
    }

    companion object {
        private const val KEY_CLIENT_SOURCE = "x-client-source"

        // TODO: [Medium] Hide from code
        private const val VALUE_CLIENT_SOURCE = "phoh8Meimu1uQuoH"
    }
}
