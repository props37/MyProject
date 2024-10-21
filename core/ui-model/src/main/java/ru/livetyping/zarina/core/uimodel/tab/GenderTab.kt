package ru.livetyping.zarina.core.uimodel.tab

import ru.livetyping.zarina.core.domain.model.gender.Gender

public enum class GenderTab {
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
