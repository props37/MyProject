package ru.livetyping.zarina.feature.signin.ui.impl.signin.model

internal enum class SignInType {
    EMAIL, PHONE;

    companion object {
        fun getAll(): List<SignInType> = listOf(EMAIL, PHONE)
    }
}
