package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.domain.model.category.CategoryPath
import ru.livetyping.zarina.core.analytics.model.CategoryPath as AppMetricaCategoryPath

public fun CategoryPath.toAppMetricaCategoryPath(): AppMetricaCategoryPath {
    return AppMetricaCategoryPath(
        gender = gender.toAppMetricaGender(),
        path = path.map { it.toAppMetricaCategory() },
    )
}
