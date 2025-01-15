package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.gender.Gender

@Serializable
@JvmInline
internal value class GenderDto(val value: String) {
    fun toGender(): Gender? = when (value) {
        FEMALE -> Gender.FEMALE
        MALE -> Gender.MALE
        else -> null
    }

    companion object {
        fun from(gender: Gender): GenderDto = when (gender) {
            Gender.FEMALE -> GenderDto(FEMALE)
            Gender.MALE -> GenderDto(MALE)
        }

        private const val FEMALE = "F"
        private const val MALE = "M"
    }
}
