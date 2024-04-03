package ru.livetyping.zarina.ui.screen.catalog

import ru.livetyping.zarina.usecase.category.GetCategoriesFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserContentGenderFlowUseCase
import ru.livetyping.zarina.usecase.user.SetUserContentGenderUseCase
import javax.inject.Inject

class CatalogInteractor @Inject constructor(
    val getCategoriesFlow: GetCategoriesFlowUseCase,
    val getUserContentGenderFlow: GetUserContentGenderFlowUseCase,
    val setUserContentGender: SetUserContentGenderUseCase,
)
