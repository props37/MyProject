package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.analytics.model.Gender as AppMetricaGender

public fun Gender.toAppMetricaGender(): AppMetricaGender {
    return when (this) {
        Gender.FEMALE -> AppMetricaGender.FEMALE
        Gender.MALE -> AppMetricaGender.MALE
    }
}
