// ui/screens/ProductDetailScreen.kt
package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    productId: Int,
    onBack: () -> Unit,
    vm: ProductDetailViewModel = hiltViewModel()
) {
    val product by vm.product(productId).collectAsState(initial = null)

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(product?.name ?: "Cargando…")
        Spacer(Modifier.height(8.dp))
        Text(product?.description ?: "—")
        Spacer(Modifier.height(8.dp))
        Text(product?.price?.let { "$${String.format("%.2f", it)}" } ?: "—")
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}
