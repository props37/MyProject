package ru.zarina.zarina.data.rework.common.remote.headerprovider

interface HeaderProvider {
    fun provide(): Map<String, String>
}
