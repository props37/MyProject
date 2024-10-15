package ru.livetyping.zarina.core.domain.model.common

public enum class Gender {
    FEMALE,
    MALE;

    public companion object {
        public fun getDefault(): Gender = FEMALE
    }
}
