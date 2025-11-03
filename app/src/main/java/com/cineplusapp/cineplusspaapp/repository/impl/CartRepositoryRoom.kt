package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.local.dao.CartItemDao
import com.cineplusapp.cineplusspaapp.data.local.entity.CartItemEntity
import com.cineplusapp.cineplusspaapp.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryRoom @Inject constructor(
    private val dao: CartItemDao
) : CartRepository {

    override suspend fun addLocal(item: CartItemEntity) {
        // nos aseguramos de estampar updatedAt y synced=false al agregar/actualizar
        dao.upsert(item.copy(
            synced = false,
            updatedAt = System.currentTimeMillis()
        ))
    }

    override fun pending(): Flow<List<CartItemEntity>> =
        dao.findPendingFlow()

    override suspend fun markSynced(id: Long) {
        dao.markSynced(id)
    }
}