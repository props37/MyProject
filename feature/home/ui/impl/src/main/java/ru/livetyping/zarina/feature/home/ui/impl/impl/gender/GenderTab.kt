package ru.livetyping.zarina.feature.home.ui.impl.impl.gender

import ru.livetyping.zarina.core.domain.model.gender.Gender

internal enum class GenderTab {
    WOMEN,
    MEN;

    fun toGender(): Gender {
        return when (this) {
            WOMEN -> Gender.FEMALE
            MEN -> Gender.MALE
        }
    }

    companion object {
        fun getTabs(): List<GenderTab> {
            return entries.toList()
        }

        fun from(gender: Gender): GenderTab {
            return when (gender) {
                Gender.FEMALE -> WOMEN
                Gender.MALE -> MEN
            }
        }
    }
}
