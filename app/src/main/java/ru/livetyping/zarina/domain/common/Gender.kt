package ru.livetyping.zarina.domain.common

enum class Gender {
    FEMALE,
    MALE;

    companion object {
        fun getDefault(): Gender = FEMALE
    }
}
