package com.cineplusapp.cineplusspaapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cineplusapp.cineplusspaapp.ui.profile.ProfileScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment

/**
 * Define las rutas de navegación de la aplicación.
 */
object Routes {
    const val PROFILE = "profile"
    const val HOME = "home" // Añadimos una ruta inicial de ejemplo
}

@Composable
fun AppNavigation() {
    // Inicializa el controlador de navegación para todo el grafo
    val navController = rememberNavController()

    // NavHost define el contenedor de navegación y las pantallas (composables)
    NavHost(
        navController = navController,
        startDestination = Routes.HOME // Define la pantalla que se muestra al inicio
    ) {
        // 1. Ruta de la pantalla de Perfil
        composable(Routes.PROFILE) {
            ProfileScreen()
        }

        // 2. Ruta de la pantalla Home (Ejemplo para tener una pantalla inicial)
        composable(Routes.HOME) {
            // Ejemplo de una pantalla simple con el botón de navegación
            HomePlaceholderScreen(
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        // Aquí podrías añadir más rutas (ej: composable(Routes.MOVIES) { ... })
    }
}

/**
 * Pantalla de ejemplo que contiene el botón de navegación.
 */
@Composable
fun HomePlaceholderScreen(
    onNavigateToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido a CinePlus App", Modifier.padding(bottom = 32.dp))

        // El botón para navegar a la pantalla de Perfil
        Button(onClick = onNavigateToProfile) {
            Text("Ver Perfil")
        }
    }
}