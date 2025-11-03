// ui/screens/ProductDetailScreen.kt
package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.ProductViewModel

@Composable
fun ProductDetailScreen(
    productId: Int,
    onBack: () -> Unit,
    vm: ProductViewModel = hiltViewModel()
) {
    val product by vm.product(productId).collectAsState()
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(product?.name ?: "Cargando…")
        Spacer(Modifier.height(8.dp))
        Text(product?.description ?: "—")
        Spacer(Modifier.height(8.dp))
        Text(product?.price?.let { "$${"%.2f".format(it)}" } ?: "—")
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}
