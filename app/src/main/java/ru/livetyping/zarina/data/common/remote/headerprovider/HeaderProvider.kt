package ru.livetyping.zarina.data.common.remote.headerprovider

interface HeaderProvider {
    fun provide(): Map<String, String>
}
