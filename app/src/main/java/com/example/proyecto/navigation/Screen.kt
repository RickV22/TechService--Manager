package com.example.proyecto.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object ClienteList : Screen("cliente_list")
    object ClienteForm : Screen("cliente_form?clienteId={clienteId}") {
        fun createRoute(clienteId: Long? = null) =
            if (clienteId != null) "cliente_form?clienteId=$clienteId"
            else "cliente_form?clienteId=-1"
    }
    object ClienteDetail : Screen("cliente_detail/{clienteId}") {
        fun createRoute(clienteId: Long) = "cliente_detail/$clienteId"
    }
    object EquipoList : Screen("equipo_list/{clienteId}") {
        fun createRoute(clienteId: Long) = "equipo_list/$clienteId"
    }
    object EquipoForm : Screen("equipo_form/{clienteId}?equipoId={equipoId}") {
        fun createRoute(clienteId: Long, equipoId: Long? = null) =
            if (equipoId != null) "equipo_form/$clienteId?equipoId=$equipoId"
            else "equipo_form/$clienteId?equipoId=-1"
    }
    object OrdenList : Screen("orden_list/{equipoId}") {
        fun createRoute(equipoId: Long) = "orden_list/$equipoId"
    }
    object OrdenForm : Screen("orden_form/{equipoId}?ordenId={ordenId}") {
        fun createRoute(equipoId: Long, ordenId: Long? = null) =
            if (ordenId != null) "orden_form/$equipoId?ordenId=$ordenId"
            else "orden_form/$equipoId?ordenId=-1"
    }
    object OrdenDetail : Screen("orden_detail/{ordenId}") {
        fun createRoute(ordenId: Long) = "orden_detail/$ordenId"
    }
    object Configuracion : Screen("configuracion")
}