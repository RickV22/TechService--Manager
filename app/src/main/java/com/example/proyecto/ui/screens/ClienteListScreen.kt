package com.example.proyecto.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.local.entity.Cliente
import com.example.proyecto.ui.components.EmptyState
import com.example.proyecto.ui.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteListScreen(
    viewModel: ClienteViewModel,
    onClienteClick: (Long) -> Unit,
    onAddCliente: () -> Unit,
    onBack: () -> Unit
) {

    val clientes    by viewModel.clientes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes") },
                navigationIcon = {
                    ////Boton para volver atras
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        ////Boton para agregar un cliente nuevo
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCliente) {
                Icon(Icons.Default.Add, "Agregar cliente")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            ////Barra de busqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::setSearchQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar cliente...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true
            )

            if (clientes.isEmpty()) {
                EmptyState("No hay clientes registrados.\nPresiona + para agregar uno.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(clientes, key = { it.id }) { cliente ->
                        ClienteItem(
                            cliente = cliente,
                            onClick = { onClienteClick(cliente.id) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ClienteItem(cliente: Cliente, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(cliente.nombre, style = MaterialTheme.typography.titleMedium)
        },
        supportingContent = {
            Text(cliente.telefono)
        },
        leadingContent = {
            Icon(
                Icons.Default.AccountCircle,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        },
        trailingContent = {
            Icon(Icons.Default.ChevronRight, null)
        }
    )
}