package com.example.proyecto.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyecto.ui.screens.*
import com.example.proyecto.ui.viewmodel.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    clienteVM: ClienteViewModel,
    equipoVM: EquipoViewModel,
    ordenVM: OrdenServicioViewModel,
    configVM: ConfiguracionViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        enterTransition = { 
            fadeIn(tween(300, easing = FastOutSlowInEasing)) + 
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300, easing = FastOutSlowInEasing)) 
        },
        exitTransition = { 
            fadeOut(tween(300, easing = FastOutSlowInEasing)) + 
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300, easing = FastOutSlowInEasing)) 
        },
        popEnterTransition = { 
            fadeIn(tween(300, easing = FastOutSlowInEasing)) + 
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300, easing = FastOutSlowInEasing)) 
        },
        popExitTransition = { 
            fadeOut(tween(300, easing = FastOutSlowInEasing)) + 
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300, easing = FastOutSlowInEasing)) 
        }
    ) {

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                clienteVM = clienteVM,
                ordenVM = ordenVM,
                configVM = configVM,
                onNavigateToClientes = { navController.navigate(Screen.ClienteList.route) },
                onNavigateToConfig = { navController.navigate(Screen.Configuracion.route) }
            )
        }

        composable(Screen.ClienteList.route) {
            ClienteListScreen(
                viewModel = clienteVM,
                onClienteClick = { id -> navController.navigate(Screen.ClienteDetail.createRoute(id)) },
                onAddCliente = { navController.navigate(Screen.ClienteForm.createRoute()) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ClienteForm.route,
            arguments = listOf(navArgument("clienteId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStack ->
            val clienteId = backStack.arguments?.getLong("clienteId").takeIf { it != -1L }
            ClienteFormScreen(
                clienteId = clienteId,
                viewModel = clienteVM,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ClienteDetail.route,
            arguments = listOf(navArgument("clienteId") { type = NavType.LongType })
        ) { backStack ->
            val clienteId = backStack.arguments!!.getLong("clienteId")
            ClienteDetailScreen(
                clienteId = clienteId,
                clienteVM = clienteVM,
                onEditCliente = { navController.navigate(Screen.ClienteForm.createRoute(clienteId)) },
                onViewEquipos = { navController.navigate(Screen.EquipoList.createRoute(clienteId)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EquipoList.route,
            arguments = listOf(navArgument("clienteId") { type = NavType.LongType })
        ) { backStack ->
            val clienteId = backStack.arguments!!.getLong("clienteId")
            EquipoListScreen(
                clienteId = clienteId,
                viewModel = equipoVM,
                onEquipoClick = { equipoId -> navController.navigate(Screen.OrdenList.createRoute(equipoId)) },
                onAgregarEquipo = { navController.navigate(Screen.EquipoForm.createRoute(clienteId)) },
                onEditarEquipo = { equipoId -> navController.navigate(Screen.EquipoForm.createRoute(clienteId, equipoId)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EquipoForm.route,
            arguments = listOf(
                navArgument("clienteId") { type = NavType.LongType },
                navArgument("equipoId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStack ->
            val clienteId = backStack.arguments!!.getLong("clienteId")
            val equipoId  = backStack.arguments?.getLong("equipoId").takeIf { it != -1L }
            EquipoFormScreen(
                clienteId = clienteId,
                equipoId  = equipoId,
                viewModel = equipoVM,
                onGuardar = { navController.popBackStack() },
                onBack    = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.OrdenList.route,
            arguments = listOf(navArgument("equipoId") { type = NavType.LongType })
        ) { backStack ->
            val equipoId = backStack.arguments!!.getLong("equipoId")
            OrdenListScreen(
                equipoId = equipoId,
                equipoVM = equipoVM,
                ordenVM  = ordenVM,
                alVerOrden = { ordenId -> navController.navigate(Screen.OrdenDetail.createRoute(ordenId)) },
                alAgregarOrden = { navController.navigate(Screen.OrdenForm.createRoute(equipoId)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.OrdenForm.route,
            arguments = listOf(
                navArgument("equipoId") { type = NavType.LongType },
                navArgument("ordenId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStack ->
            val equipoId = backStack.arguments!!.getLong("equipoId")
            val ordenId  = backStack.arguments?.getLong("ordenId").takeIf { it != -1L }
            OrdenFormScreen(
                equipoId  = equipoId,
                ordenId   = ordenId,
                viewModel = ordenVM,
                onGuardar = { navController.popBackStack() },
                onBack    = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.OrdenDetail.route,
            arguments = listOf(navArgument("ordenId") { type = NavType.LongType })
        ) { backStack ->
            val ordenId = backStack.arguments!!.getLong("ordenId")
            OrdenDetailScreen(
                ordenId       = ordenId,
                equipoVM      = equipoVM,
                ordenVM       = ordenVM,
                clienteVM     = clienteVM,
                alEditarOrden = { eqId -> navController.navigate(Screen.OrdenForm.createRoute(eqId, ordenId)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Configuracion.route) {
            ConfiguracionScreen(
                viewModel = configVM,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}
