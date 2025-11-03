// ui/screens/ProductStoreScreen.kt
package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.ProductViewModel

@Composable
fun ProductStoreScreen(
    onProductClick: (Int) -> Unit,
    vm: ProductViewModel = hiltViewModel()
) {
    val products by vm.products.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tienda", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        products.forEach { p ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onProductClick(p.id) }
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(p.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("${p.description}  •  $${"%.2f".format(p.price)}")
                }
            }
        }
        if (products.isEmpty()) {
            Text("Sin productos cargados.")
        }
    }
}
