package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.analytics.model.Category as AppMetricaCategory

public fun Category.toAppMetricaCategory(): AppMetricaCategory {
    return AppMetricaCategory(name)
}
