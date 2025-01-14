package ru.livetyping.zarina.data.cart.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.domain.cache.CacheExpirationPolicy
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.cache.CacheUpdatePolicy
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.data.cart.impl.local.CartLocalDataSource
import ru.livetyping.zarina.data.cart.impl.model.CartProductIds
import ru.livetyping.zarina.data.cart.impl.remote.CartRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

internal class CartRepositoryImpl @Inject constructor(
    private val remoteDataSource: CartRemoteDataSource,
    private val localDataSource: CartLocalDataSource,
) : CartRepository {
    override fun getCartFlow(cartType: CartType, cityKladrId: KladrId?): Flow<Cart> {
        return remoteDataSource.getCartFlow(cartType, cityKladrId)
    }

    override fun getCartProductIdsFlow(cachePolicy: CachePolicy): Flow<Set<Product.Id>> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getCartProductIdsFlow()
            is CachePolicy.LocalFirstThenRemote -> {
                getCartProductIdsFlowLocalFirstThenRemote(cachePolicy)
            }

            is CachePolicy.Remote -> getCartProductIdsFlowRemote(cachePolicy)
        }
    }

    override fun areCartProductIdsFetched(): Boolean {
        return localDataSource.areCartProductIdsFetched()
    }

    override fun getCartProductCountFlow(): Flow<Int> {
        return localDataSource.getCartProductCountFlow()
    }

    override suspend fun addProductToCart(productId: Product.Id, barcode: Barcode, count: Int) {
        val cartProductCount = remoteDataSource.addProductToCart(barcode, count)
        localDataSource.addProductToCart(productId)
        localDataSource.setCartProductCount(cartProductCount.value)
    }

    override suspend fun removeProductFromCart(productId: Product.Id, barcode: Barcode) {
        val cartProductCount = remoteDataSource.remoteProductFromCart(barcode)
        localDataSource.removeProductFromCart(productId)
        localDataSource.setCartProductCount(cartProductCount.value)
    }

    override suspend fun clearCart() {
        remoteDataSource.clearCart()
        localDataSource.setCartProductIds(emptySet())
        localDataSource.setCartProductCount(0)
    }

    override fun clear() {
        localDataSource.clear()
    }

    private fun getCartProductIdsFlowLocalFirstThenRemote(
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<Set<Product.Id>> {
        // TODO: [Low] Add support for CacheExpirationPolicy
        Timber.tag(TAG).w("Cart product IDs CacheExpirationPolicy is not supported, fallback to ${CacheExpirationPolicy.UNLIMITED}")
        return localDataSource.getCartProductIdsFlow()
            .map { cached ->
                if (!localDataSource.areCartProductIdsFetched()) {
                    val productIds = remoteDataSource.getCartProductIdsFlow().firstOrNull()
                    checkNotNull(productIds) { "Failed to fetch cart product IDs" }
                    cartProductIdsCacheUpdatePolicyImpl(productIds, cachePolicy.updatePolicy)
                    productIds.cartProductIds
                } else {
                    cached
                }
            }
    }

    private fun getCartProductIdsFlowRemote(
        cachePolicy: CachePolicy.Remote,
    ): Flow<Set<Product.Id>> {
        return remoteDataSource.getCartProductIdsFlow()
            .onEach { productIds ->
                cartProductIdsCacheUpdatePolicyImpl(productIds, cachePolicy.updatePolicy)
            }
            .map { it.cartProductIds }
    }

    private fun cartProductIdsCacheUpdatePolicyImpl(
        cartProductIds: CartProductIds,
        policy: CacheUpdatePolicy,
    ) {
        when (policy) {
            CacheUpdatePolicy.NONE -> Unit
            CacheUpdatePolicy.CLEAR -> clearLocalCartProductIds()
            CacheUpdatePolicy.UPDATE -> setLocalFetchedCartProductIds(cartProductIds)
        }
    }

    private fun setLocalFetchedCartProductIds(cartProductIds: CartProductIds) {
        localDataSource.setCartProductIds(cartProductIds.cartProductIds)
        localDataSource.setCartProductCount(cartProductIds.cartProductCount)
        localDataSource.setAreCartProductIdsFetched(true)
    }

    private fun clearLocalCartProductIds() {
        localDataSource.setCartProductIds(emptySet())
        localDataSource.setCartProductCount(0)
        localDataSource.setAreCartProductIdsFetched(false)
    }

    private companion object {
        private const val TAG = "CartRepositoryImpl"
    }
}
