package ru.livetyping.zarina.domain.category

import ru.livetyping.zarina.domain.common.Gender

data class CategoryPath(
    val gender: Gender,
    val path: List<Category>,
)
