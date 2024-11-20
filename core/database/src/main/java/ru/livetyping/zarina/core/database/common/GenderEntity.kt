package ru.livetyping.zarina.core.database.common

import ru.livetyping.zarina.core.domain.model.gender.Gender

public enum class GenderEntity {
    FEMALE,
    MALE;

    public fun toGender(): Gender = when (this) {
        FEMALE -> Gender.FEMALE
        MALE -> Gender.MALE
    }

    public companion object {
        public fun from(gender: Gender): GenderEntity = when (gender) {
            Gender.FEMALE -> FEMALE
            Gender.MALE -> MALE
        }
    }
}
