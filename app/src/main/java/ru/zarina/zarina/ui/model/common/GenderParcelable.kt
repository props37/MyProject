package ru.zarina.zarina.ui.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.common.Gender

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
