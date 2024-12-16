package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.category.GetCategoriesFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.GetLastContentGenderFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase
import javax.inject.Inject

internal class CatalogDeps @Inject constructor(
    val getLastContentGenderFlow: GetLastContentGenderFlowUseCase,
    val getCategoriesFlow: GetCategoriesFlowUseCase,
    val setLastContentGender: SetLastContentGenderUseCase,
)
