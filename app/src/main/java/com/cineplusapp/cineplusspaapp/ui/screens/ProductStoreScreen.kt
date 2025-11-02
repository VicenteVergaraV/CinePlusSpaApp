package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cineplusapp.cineplusspaapp.data.model.Product
import com.cineplusapp.cineplusspaapp.data.model.ProductType
import com.cineplusapp.cineplusspaapp.viewmodel.ProductViewModel

@Composable
fun ProductStoreScreen(
    onProductClick: (Int) -> Unit,
    vm: ProductViewModel = hiltViewModel()
) {
    var tab by remember { mutableStateOf(0) }
    val tabs = listOf("Cabritas", "Bebestibles", "Promos")
    val type = when (tab) {
        0 -> ProductType.POPCORN
        1 -> ProductType.DRINK
        else -> ProductType.COMBO
    }

    val products by vm.products.collectAsState()
    LaunchedEffect(Unit) { vm.seed() }
    LaunchedEffect(type) { vm.setFilter(type) }

    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tab) {
            tabs.forEachIndexed { i, title ->
                Tab(selected = tab == i, onClick = { tab = i }, text = { Text(title) })
            }
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(160.dp), modifier = Modifier.fillMaxSize().padding(12.dp)) {
            items(products) { p -> ProductCard(p) { onProductClick(p.id) } }
        }
    }
}

@Composable
private fun ProductCard(p: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp).clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            if (p.imageUrl != null) {
                Image(rememberAsyncImagePainter(p.imageUrl), null, Modifier.fillMaxWidth().height(120.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(p.name, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            Text(p.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            Spacer(Modifier.height(6.dp))
            Text("$ ${"%.2f".format(p.price)}", style = MaterialTheme.typography.titleSmall)
        }
    }
}
