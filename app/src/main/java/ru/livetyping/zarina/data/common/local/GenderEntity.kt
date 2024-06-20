package ru.livetyping.zarina.data.common.local

import ru.livetyping.zarina.domain.common.Gender

enum class GenderEntity {
    FEMALE,
    MALE;

    fun toGender(): Gender = when (this) {
        FEMALE -> Gender.FEMALE
        MALE -> Gender.MALE
    }

    companion object {
        fun from(gender: Gender): GenderEntity = when (gender) {
            Gender.FEMALE -> FEMALE
            Gender.MALE -> MALE
        }
    }
}
