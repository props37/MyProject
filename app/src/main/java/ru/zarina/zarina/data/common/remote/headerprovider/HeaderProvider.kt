package ru.zarina.zarina.data.common.remote.headerprovider

interface HeaderProvider {
    fun provide(): Map<String, String>
}
