package ru.zarina.zarina.ui.screen.catalog

import ru.zarina.zarina.usecase.category.GetCategoriesFlowUseCase
import ru.zarina.zarina.usecase.user.GetUserContentGenderFlowUseCase
import ru.zarina.zarina.usecase.user.SetUserContentGenderUseCase
import javax.inject.Inject

class CatalogInteractor @Inject constructor(
    val getCategoriesFlow: GetCategoriesFlowUseCase,
    val getUserContentGenderFlow: GetUserContentGenderFlowUseCase,
    val setUserContentGender: SetUserContentGenderUseCase,
)
