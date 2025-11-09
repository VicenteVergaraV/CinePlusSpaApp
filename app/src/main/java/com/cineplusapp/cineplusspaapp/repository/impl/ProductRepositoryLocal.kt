// repository/impl/ProductRepositoryLocal.kt
package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.model.Product
import com.cineplusapp.cineplusspaapp.data.model.ProductType
import com.cineplusapp.cineplusspaapp.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryLocal @Inject constructor() : ProductRepository {

    // Product(id, name, description, imageUrl: String?, price: Double, type: ProductType)
    private val store = MutableStateFlow(
        listOf(
            Product(
                id = 1,
                name = "Pop Corn",
                description = "Combo Clasico",
                imageUrl = null,
                price = 2900,
                type = ProductType.POPCORN
            ),
            Product(
                id = 2,
                name = "Bebida 500ml",
                description = "Gaseosa",
                imageUrl = null,
                price = 1500,
                type = ProductType.DRINK
            ),
            Product(
                id = 3,
                name = "Gorra CinePlus",
                description = "Talla M",
                imageUrl = null,
                price = 2000,
                type = ProductType.COMBO
            )
        )
    )

    override fun list(filter: ProductType?): Flow<List<Product>> =
        if (filter == null) store else store.map { it.filter { p -> p.type == filter } }

    override fun byId(id: Int): Flow<Product?> =
        store.map { it.find { p -> p.id == id } }
}
