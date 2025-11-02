package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cineplusapp.cineplusspaapp.viewmodel.ProductViewModel

@Composable
fun ProductDetailScreen(
    productId: Int,
    onBack: () -> Unit,
    vm: ProductViewModel = hiltViewModel()
) {
    val product by vm.product(productId).collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        product?.let { p ->
            if (p.imageUrl != null) {
                Image(rememberAsyncImagePainter(p.imageUrl), null, Modifier.fillMaxWidth().height(200.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(p.name, style = MaterialTheme.typography.headlineSmall)
            Text(p.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text("Precio: $ ${"%.2f".format(p.price)}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBack) { Text("Volver") }
        } ?: Text("Cargando…")
    }
}
