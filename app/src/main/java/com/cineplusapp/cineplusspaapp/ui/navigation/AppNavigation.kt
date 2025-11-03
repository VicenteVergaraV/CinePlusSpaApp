// AppNavigation.kt
package com.cineplusapp.cineplusspaapp.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.ui.profile.EditProfileScreen
import com.cineplusapp.cineplusspaapp.ui.profile.ProfileScreen
import com.cineplusapp.cineplusspaapp.ui.screens.*
import com.cineplusapp.cineplusspaapp.viewmodel.HomeViewModel

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
    val token by session.accessTokenFlow.collectAsState(initial = null)
    val start = Routes.LOGIN

    LaunchedEffect(token) {
        if (token.isNullOrBlank()) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        } else if (navController.currentDestination?.route == Routes.LOGIN) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = start) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onGoRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistered = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
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

        composable(Routes.USERS) {
            UsersScreen(onClickUser = { id -> navController.navigate("user_detail/$id") })
        }

        composable(
            route = Routes.USER_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val userId = backStack.arguments?.getInt("id") ?: 0
            UserDetailScreen(userId = userId, onBack = { navController.popBackStack() })
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

        composable(Routes.CARTELERA) {
            CarteleraScreen(onMovieClick = { id -> navController.navigate("movie_detail/$id") })
        }

        composable(
            Routes.MOVIE_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt("id") ?: 0
            MovieDetailScreen(movieId = id, onBack = { navController.popBackStack() })
        }

        composable(Routes.STORE) {
            ProductStoreScreen(onProductClick = { id -> navController.navigate("product_detail/$id") })
        }

        composable(
            Routes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt("id") ?: 0
            ProductDetailScreen(productId = id, onBack = { navController.popBackStack() })
        }

        composable(Routes.NEARBY) {
            NearbyCinemasScreen() // asegúrate de tener este composable creado
        }
    }
}
