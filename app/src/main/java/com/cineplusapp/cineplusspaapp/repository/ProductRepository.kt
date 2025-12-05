package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.domain.model.Product
import com.cineplusapp.cineplusspaapp.domain.model.ProductType
import kotlinx.coroutines.flow.Flow
interface ProductRepository {
    fun list(filter: ProductType? = null): Flow<List<Product>>
    fun byId(id: Int): Flow<Product?>
}
