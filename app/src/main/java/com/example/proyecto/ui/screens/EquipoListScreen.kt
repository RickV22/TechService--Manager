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
import com.example.proyecto.data.local.entity.Equipo
import com.example.proyecto.ui.components.DeleteConfirmDialog
import com.example.proyecto.ui.components.EmptyState
import com.example.proyecto.ui.viewmodel.EquipoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipoListScreen(
    clienteId: Long,
    viewModel: EquipoViewModel,
    onEquipoClick: (Long) -> Unit,
    onAgregarEquipo: () -> Unit,
    onEditarEquipo: (Long) -> Unit,
    onBack: () -> Unit
) {
    val listaEquipos by viewModel.getEquiposByCliente(clienteId).collectAsState()
    var equipoAEliminar by remember { mutableStateOf<Equipo?>(null) }

    equipoAEliminar?.let { equipo ->
        DeleteConfirmDialog(
            titulo = "Eliminar equipo",
            mensaje = "¿Eliminar ${equipo.marca} ${equipo.modelo}? " +
                    "Se borrarán todas sus órdenes de servicio.",
            onConfirm = {
                viewModel.deleteEquipo(equipo)
                equipoAEliminar = null
            },
            onDismiss = {
                equipoAEliminar = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipos") },
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
            FloatingActionButton(onClick = onAgregarEquipo) {
                Icon(Icons.Default.Add, "Agregar equipo")
            }
        }
    ) { padding ->

        if (listaEquipos.isEmpty()) {
            Box(Modifier.padding(padding)) {
                EmptyState("No hay equipos registrados.\nPresiona + para agregar uno.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(listaEquipos, key = { it.id }) { equipo ->
                    FilaEquipo(
                        equipo = equipo,
                        alHacerClick = { onEquipoClick(equipo.id) },
                        alEditarClick = { onEditarEquipo(equipo.id) },
                        alEliminarClick = { equipoAEliminar = equipo }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun FilaEquipo(
    equipo: Equipo,
    alHacerClick: () -> Unit,
    alEditarClick: () -> Unit,
    alEliminarClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = alHacerClick),
        headlineContent = {
            Text("${equipo.marca} ${equipo.modelo}")
        },
        supportingContent = {
            Text("${equipo.tipo} • S/N: ${equipo.numeroSerie}")
        },
        leadingContent = {
            Icon(
                Icons.Default.Devices,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        },
        trailingContent = {
            Row {
                ////editar el equipo
                IconButton(onClick = alEditarClick) {
                    Icon(
                        Icons.Default.Edit,
                        "Editar",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                ////eliminar el equipo
                IconButton(onClick = alEliminarClick) {
                    Icon(
                        Icons.Default.Delete,
                        "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}