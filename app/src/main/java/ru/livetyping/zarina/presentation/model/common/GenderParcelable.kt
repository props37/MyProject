package ru.livetyping.zarina.presentation.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Gender

@Serializable
@Parcelize
enum class GenderParcelable : Parcelable {
    FEMALE,
    MALE;

    fun toGender(): Gender = when (this) {
        FEMALE -> Gender.FEMALE
        MALE -> Gender.MALE
    }

    companion object {
        fun from(gender: Gender): GenderParcelable = when (gender) {
            Gender.FEMALE -> FEMALE
            Gender.MALE -> MALE
        }
    }
}
