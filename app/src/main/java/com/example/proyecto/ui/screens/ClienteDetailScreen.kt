package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto.ui.components.DeleteConfirmDialog
import com.example.proyecto.ui.viewmodel.ClienteViewModel
import com.example.proyecto.utils.abrirMaps
import com.example.proyecto.utils.llamarCliente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteDetailScreen(
    clienteId: Long,
    clienteVM: ClienteViewModel,
    onEditCliente: () -> Unit,
    onViewEquipos: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val cliente by clienteVM.getClienteById(clienteId).collectAsState(initial = null)

    var showDeleteDialog by remember { mutableStateOf(false) }

    ////componente en CommonComponents
    if (showDeleteDialog && cliente != null) {
        DeleteConfirmDialog(
            titulo = "Eliminar cliente",
            mensaje = "¿Eliminar a ${cliente!!.nombre}? Se borrarán todos sus equipos y órdenes.",
            onConfirm = {
                clienteVM.deleteCliente(cliente!!)
                onBack()
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del cliente") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {

                    IconButton(onClick = onEditCliente) {
                        Icon(Icons.Default.Edit, "Editar")
                    }

                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        cliente?.let { c ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ////tarjetas
                Card(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            c.nombre,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "ID #${c.id}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                ////Filas
                ContactInfoRow(Icons.Default.Phone, "Teléfono", c.telefono)
                ContactInfoRow(Icons.Default.Email, "Correo",
                    c.email.ifBlank { "No registrado" })
                ContactInfoRow(Icons.Default.LocationOn, "Dirección",
                    c.direccion.ifBlank { "No registrada" })

                HorizontalDivider()

                Text(
                    "Acciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                ////funciones de Extensions
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón llamar - abre la app de teléfono con el número
                    OutlinedButton(
                        onClick = { context.llamarCliente(c.telefono) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Call, null)
                        Spacer(Modifier.width(4.dp))
                        Text("Llamar")
                    }

                    ////boton Maps
                    OutlinedButton(
                        onClick = { context.abrirMaps(c.direccion) },
                        modifier = Modifier.weight(1f),
                        enabled = c.direccion.isNotBlank()
                    ) {
                        Icon(Icons.Default.Map, null)
                        Spacer(Modifier.width(4.dp))
                        Text("Maps")
                    }
                }

                Spacer(Modifier.weight(1f))

                ////para ver los equipos
                Button(
                    onClick = onViewEquipos,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(Icons.Default.Devices, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ver equipos del cliente")
                }
            }
        }
    }
}

@Composable
private fun ContactInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}