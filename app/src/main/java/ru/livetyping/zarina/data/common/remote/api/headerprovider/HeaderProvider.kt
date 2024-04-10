package ru.livetyping.zarina.data.common.remote.api.headerprovider

interface HeaderProvider {
    fun provide(): Map<String, String>
}
