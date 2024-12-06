package ru.livetyping.zarina.core.uimodel.tab

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.gender.Gender

// Marked as stable on config/compose/stability_config.txt
@Serializable
@Parcelize
public enum class GenderTab : Parcelable {
    WOMEN,
    MEN;

    public fun toGender(): Gender {
        return when (this) {
            WOMEN -> Gender.FEMALE
            MEN -> Gender.MALE
        }
    }

    public companion object {
        public fun getTabs(): List<GenderTab> {
            return entries.toList()
        }

        public fun from(gender: Gender): GenderTab {
            return when (gender) {
                Gender.FEMALE -> WOMEN
                Gender.MALE -> MEN
            }
        }
    }
}
