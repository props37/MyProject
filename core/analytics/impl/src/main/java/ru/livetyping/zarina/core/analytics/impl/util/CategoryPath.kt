package ru.livetyping.zarina.core.analytics.impl.util

import ru.livetyping.zarina.core.analytics.model.Category
import ru.livetyping.zarina.core.analytics.model.CategoryPath
import ru.livetyping.zarina.core.analytics.model.Gender

internal fun CategoryPath.toNameList(): List<String> {
    val path = this
    return buildList {
        val section = when (path.gender) {
            Gender.FEMALE -> Category.CATEGORY_WOMEN
            Gender.MALE -> Category.CATEGORY_MEN
        }
        add(section)
        addAll(path.path.map { it.name })
    }
}
