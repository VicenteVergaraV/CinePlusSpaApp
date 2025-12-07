// AppNavigation.kt
package com.cineplusapp.cineplusspaapp.ui.navigation

import MovieDetailScreen
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.ui.profile.EditProfileScreen
import com.cineplusapp.cineplusspaapp.ui.profile.ProfileScreen
import com.cineplusapp.cineplusspaapp.ui.screens.*
import com.cineplusapp.cineplusspaapp.viewmodel.HomeViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.MovieViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.ProductDetailViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val USERS = "users"
    const val USER_DETAIL = "user_detail/{id}"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val CARTELERA = "cartelera"
    const val MOVIE_DETAIL = "movie_detail/{id}"
    const val STORE = "store"
    const val PRODUCT_DETAIL = "product_detail/{id}"
    const val NEARBY = "nearby"
}

@Composable
fun AppNavigation(session: SessionManager) {
    val navController: NavHostController = rememberNavController()
    val scope = rememberCoroutineScope()

    // null = todavía cargando desde DataStore
    val accessToken: String? by session.accessTokenFlow.collectAsState(initial = null)

    // Mientras se inicializa la sesión, solo mostramos Splash
    if (accessToken == null) {
        SplashScreen()
        return
    }

    // Determinar pantalla inicial SOLO para el arranque
    val startDestination = if (accessToken.isNullOrBlank()) {
        Routes.LOGIN
    } else {
        Routes.HOME
    }

    // Efecto: si el token cambia a "logeado" y estoy en LOGIN, navego a HOME
    LaunchedEffect(accessToken) {
        val currentRoute = navController.currentDestination?.route
        if (!accessToken.isNullOrBlank() && currentRoute == Routes.LOGIN) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                },
                onGoRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(onRegistered = { navController.popBackStack() })
        }

        composable(Routes.HOME) {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(
                onGoUsers = { navController.navigate(Routes.USERS) },
                onGoProfile = { navController.navigate(Routes.PROFILE) },
                onGoCartelera = { navController.navigate(Routes.CARTELERA) },
                onGoStore = { navController.navigate(Routes.STORE) },
                onGoNearby = { navController.navigate(Routes.NEARBY) },
                onLogout = {
                    vm.logout {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }



        composable(Routes.REGISTER) {
            RegisterScreen(onRegistered = { navController.popBackStack() })
        }


        composable(Routes.USERS) {
            UsersScreen(onClickUser = { id -> navController.navigate("user_detail/$id") })
        }

        composable(
            route = Routes.USER_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments!!.getInt("id")
            UserDetailScreen(userId = id, onBack = { navController.popBackStack() })
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onEdit = { navController.navigate(Routes.EDIT_PROFILE) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(onSaved = { navController.popBackStack() })
        }

        // CARTELERA
        composable(Routes.CARTELERA) {
            val vm: MovieViewModel = hiltViewModel()
            CarteleraScreen(
                vm = vm,
                onMovieClick = { id -> navController.navigate("movie_detail/$id") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.MOVIE_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments!!.getInt("id")
            val vm: MovieViewModel = hiltViewModel()
            MovieDetailScreen(
                vm = vm,
                movieId = id,
                onBack = { navController.popBackStack() }
            )
        }

        // STORE
        composable(Routes.STORE) {
            val vm: ProductViewModel = hiltViewModel()
            ProductStoreScreen(
                vm = vm,
                onProductClick = { id -> navController.navigate("product_detail/$id") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments!!.getInt("id")
            val vm: ProductDetailViewModel = hiltViewModel()
            ProductDetailScreen(
                vm = vm,
                productId = id,
                onBack = { navController.popBackStack() }
            )
        }


        composable(Routes.NEARBY) {
            NearbyCinemasScreen()
        }
    }
}
