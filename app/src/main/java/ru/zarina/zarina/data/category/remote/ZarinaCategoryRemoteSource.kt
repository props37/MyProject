package ru.zarina.zarina.data.category.remote

import ru.zarina.zarina.data.category.remote.api.IZarinaCategoryApi
import javax.inject.Inject

class ZarinaCategoryRemoteSource @Inject constructor(
    private val api: IZarinaCategoryApi,
) : ICategoryRemoteSource
