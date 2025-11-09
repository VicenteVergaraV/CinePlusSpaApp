package com.cineplusapp.cineplusspaapp.data.model

enum class ProductType { POPCORN, DRINK, COMBO }

data class Product(
    val id: Int,
    val name: String,
    val description: String = "",
    val imageUrl: String? = null,
    val price: Int = 0,
    val type: ProductType = ProductType.COMBO
)