package ru.livetyping.zarina.core.domain.model.gender

// Marked as stable on config/compose/stability_config.txt
public enum class Gender {
    FEMALE,
    MALE;

    public companion object {
        public fun getDefault(): Gender = FEMALE
    }
}
