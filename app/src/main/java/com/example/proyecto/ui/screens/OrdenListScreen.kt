package com.example.proyecto.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.local.entity.OrdenServicio
import com.example.proyecto.ui.components.DeleteConfirmDialog
import com.example.proyecto.ui.components.EmptyState
import com.example.proyecto.ui.components.EstadoChip
import com.example.proyecto.ui.viewmodel.EquipoViewModel
import com.example.proyecto.ui.viewmodel.OrdenServicioViewModel
import com.example.proyecto.utils.toFechaLegible

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenListScreen(
    equipoId: Long,
    equipoVM: EquipoViewModel,
    ordenVM: OrdenServicioViewModel,
    alVerOrden: (Long) -> Unit,
    alAgregarOrden: () -> Unit,
    onBack: () -> Unit
) {

    val equipo by equipoVM.getEquipoById(equipoId).collectAsState(initial = null)
    val listaOrdenes by ordenVM.getOrdenesByEquipo(equipoId).collectAsState()
    var ordenAEliminar by remember { mutableStateOf<OrdenServicio?>(null) }

    ordenAEliminar?.let { orden ->
        DeleteConfirmDialog(
            titulo = "Eliminar orden #${orden.id}",
            mensaje = "¿Estás seguro? Esta acción no se puede deshacer.",
            onConfirm = {
                ordenVM.deleteOrden(orden)
                ordenAEliminar = null
            },
            onDismiss = {
                ordenAEliminar = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                ////se mostra la marca y modelo
                title = {
                    Text(equipo?.let { "${it.marca} ${it.modelo}" } ?: "Órdenes")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = alAgregarOrden) {
                Icon(Icons.Default.Add, "Nueva orden")
            }
        }
    ) { padding ->

        if (listaOrdenes.isEmpty()) {
            Box(Modifier.padding(padding)) {
                EmptyState("No hay órdenes de servicio.\nPresiona + para crear una.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaOrdenes, key = { it.id }) { orden ->
                    TarjetaOrden(
                        orden = orden,
                        alHacerClick = { alVerOrden(orden.id) },
                        alEliminarClick = { ordenAEliminar = orden }
                    )
                }
                ////para no tapar la última orden
                item { Spacer(Modifier.height(72.dp)) }
            }
        }
    }
}

////tarjeta que representa una orden en la lista
@Composable
private fun TarjetaOrden(
    orden: OrdenServicio,
    alHacerClick: () -> Unit,
    alEliminarClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alHacerClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ////fila superiot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Orden #${orden.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = alEliminarClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            ////descripción del problema
            Text(
                orden.problemaReportado,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            ////fila inferior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ////cambia de color según el estado
                EstadoChip(orden.estado)
                Text(
                    orden.fechaIngreso.toFechaLegible(),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}