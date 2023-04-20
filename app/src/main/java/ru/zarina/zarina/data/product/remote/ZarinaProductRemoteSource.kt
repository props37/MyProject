package ru.zarina.zarina.data.product.remote

import ru.zarina.zarina.data.product.remote.api.IZarinaProductApi
import javax.inject.Inject

class ZarinaProductRemoteSource @Inject constructor(
    private val api: IZarinaProductApi,
) : IProductRemoteSource
