package ru.livetyping.zarina.ui.common.utils

fun adaptPhoneValue(phone: String): String {
    return buildString {
        val digits = phone
            .removePrefix("+")
            .removePrefix("7")
            .filter { it.isDigit() }
        append("+7")
        append(digits)
    }.take(PHONE_LENGTH)
}

private const val PHONE_LENGTH = 12
