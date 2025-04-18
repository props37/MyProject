package ru.livetyping.zarina.core.domain.model.category

import ru.livetyping.zarina.core.domain.model.gender.Gender

public data class CategoryPath(
    val gender: Gender,
    val path: List<Category>,
)
