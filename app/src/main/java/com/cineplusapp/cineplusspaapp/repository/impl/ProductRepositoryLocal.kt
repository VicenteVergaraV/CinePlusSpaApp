package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.local.dao.ProductDao
import com.cineplusapp.cineplusspaapp.data.mapper.toDomain
import com.cineplusapp.cineplusspaapp.data.mapper.toEntity
import com.cineplusapp.cineplusspaapp.data.model.Product
import com.cineplusapp.cineplusspaapp.data.model.ProductType
import com.cineplusapp.cineplusspaapp.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryLocal @Inject constructor(
    private val dao: ProductDao
) : ProductRepository {

    override fun list(type: ProductType?): Flow<List<Product>> =
        when (type) {
            null -> dao.observeAll().map { it.map { e -> e.toDomain() } }
            else -> dao.observeByType(type.name.lowercase()).map { it.map { e -> e.toDomain() } }
        }

    override fun byId(id: Int): Flow<Product?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun seedIfEmpty() {
        dao.clear()
        val seed = listOf(
            Product(1, "Cabritas M", "Clásicas saladas", "https://picsum.photos/300/300?random=21", 3990.0, ProductType.POPCORN),
            Product(2, "Cabritas L", "Tamaño familiar", "https://picsum.photos/300/300?random=22", 5490.0, ProductType.POPCORN),
            Product(3, "Bebida 500ml", "Gaseosa a elección", "https://picsum.photos/300/300?random=23", 2490.0, ProductType.DRINK),
            Product(4, "Agua 500ml", "Sin gas", "https://picsum.photos/300/300?random=24", 1990.0, ProductType.DRINK),
            Product(5, "Combo Pareja", "2 bebidas + cabritas L", "https://picsum.photos/300/300?random=25", 7990.0, ProductType.COMBO)
        )
        dao.upsertAll(seed.map { it.toEntity() })
    }
}
