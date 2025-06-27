package ru.livetyping.zarina.core.uimodel.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.gender.Gender

@Parcelize
@Serializable
public enum class GenderParcelable : Parcelable {
    FEMALE,
    MALE;

    public fun toGender(): Gender = when (this) {
        FEMALE -> Gender.FEMALE
        MALE -> Gender.MALE
    }

    public companion object {
        public fun from(gender: Gender): GenderParcelable = when (gender) {
            Gender.FEMALE -> FEMALE
            Gender.MALE -> MALE
        }
    }
}
