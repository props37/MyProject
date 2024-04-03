package ru.livetyping.zarina.utils

import io.ktor.util.network.UnresolvedAddressException

fun Throwable.isNetworkException(): Boolean {
    return this is java.net.SocketException || this is UnresolvedAddressException
}
