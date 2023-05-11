package ru.zarina.zarina.data.shop.remote

import ru.zarina.zarina.data.shop.remote.api.IZarinaShopApi
import javax.inject.Inject

class ZarinaShopRemoteSource @Inject constructor(
    private val api: IZarinaShopApi,
) : IShopRemoteSource
