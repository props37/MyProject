package ru.zarina.zarina.data.remote.headerprovider

interface HeaderProvider {
    fun provide(): Map<String, String>
}
