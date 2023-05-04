package ru.zarina.zarina.domain

data class Color(
    val id: String,
    val name: String,
    val code: Code,
) {

    @JvmInline
    value class Code(val value: String)

}
