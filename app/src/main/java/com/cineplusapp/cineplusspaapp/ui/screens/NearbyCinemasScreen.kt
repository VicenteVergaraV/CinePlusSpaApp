// ui/screens/NearbyCinemasScreen.kt
package com.cineplusapp.cineplusspaapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.NearbyCinemasViewModel

@Composable
fun NearbyCinemasScreen(vm: NearbyCinemasViewModel = hiltViewModel()) {
    val ctx = LocalContext.current
    var granted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                ctx, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(RequestPermission()) { ok ->
        granted = ok
    }

    LaunchedEffect(Unit) {
        if (!granted) launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    if (!granted) {
        Box(Modifier.fillMaxSize().padding(20.dp)) {
            Text("Necesitamos tu ubicación para listar cines cercanos.")
        }
        return
    }

    val ui by vm.ui.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Cines cercanos", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        if (ui.error != null) {
            Text("Error: ${ui.error}")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ui.items) { (cine, km) ->
                    ElevatedCard {
                        Column(Modifier.padding(12.dp)) {
                            Text(cine.name, style = MaterialTheme.typography.titleMedium)
                            Text(cine.address)
                            Text("Distancia: $km km")
                        }
                    }
                }
            }
        }
    }
}
