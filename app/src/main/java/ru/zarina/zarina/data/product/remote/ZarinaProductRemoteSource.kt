package ru.zarina.zarina.data.product.remote

import ru.zarina.zarina.data.product.remote.api.IZarinaProductApi
import ru.zarina.zarina.domain.Product
import javax.inject.Inject

class ZarinaProductRemoteSource @Inject constructor(
    private val api: IZarinaProductApi,
) : IProductRemoteSource {

    override suspend fun getProduct(id: Product.Id) =
        checkNotNull(api.getProduct(id.value).toDomain())

    override suspend fun getCompleteLook(product: Product) =
        api.getCompleteLook(product.id.value).toDomain()

    override suspend fun getDeliveryAvailability(product: Product) =
        checkNotNull(api.getDeliveryInfo(product.id.value).toDomain())
}
