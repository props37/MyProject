package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Gender

@Serializable
@JvmInline
value class GenderDto(val value: String) {
    fun toGender(): Gender = when (value) {
        FEMALE -> Gender.FEMALE
        MALE -> Gender.MALE
        else -> error("Unknown gender $value")
    }

    companion object {
        private const val FEMALE = "F"
        private const val MALE = "M"
    }
}
