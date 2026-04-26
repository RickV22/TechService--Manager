package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto.ui.components.EstadoChip
import com.example.proyecto.ui.viewmodel.ClienteViewModel
import com.example.proyecto.ui.viewmodel.EquipoViewModel
import com.example.proyecto.ui.viewmodel.OrdenServicioViewModel
import com.example.proyecto.utils.compartirOrden
import com.example.proyecto.utils.enviarEmail
import com.example.proyecto.utils.formatearMoneda
import com.example.proyecto.utils.generarResumen
import com.example.proyecto.utils.toFechaLegible

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenDetailScreen(
    ordenId: Long,
    equipoVM: EquipoViewModel,
    ordenVM: OrdenServicioViewModel,
    clienteVM: ClienteViewModel,
    alEditarOrden: (Long) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val orden by ordenVM.getOrdenById(ordenId).collectAsState(initial = null)

    val equipo by equipoVM.getEquipoById(
        orden?.equipoId ?: 0L
    ).collectAsState(initial = null)

    val cliente by clienteVM.getClienteById(
        equipo?.clienteId ?: 0L
    ).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orden #$ordenId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        orden?.let { alEditarOrden(it.equipoId) }
                    }) {
                        Icon(Icons.Default.Edit, "Editar")
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

        orden?.let { o ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EstadoChip(o.estado)
                    Text(
                        o.fechaIngreso.toFechaLegible(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                SeccionReporte(
                    titulo = "Problema reportado",
                    contenido = o.problemaReportado
                )
                SeccionReporte(
                    titulo = "Diagnóstico técnico",
                    contenido = o.diagnosticoTecnico.ifBlank { "Sin diagnóstico aún" }
                )
                SeccionReporte(
                    titulo = "Solución aplicada",
                    contenido = o.solucionAplicada.ifBlank { "Sin solución aún" }
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.AttachMoney,
                            null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                "Costo del servicio",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                o.costo.formatearMoneda(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                HorizontalDivider()

                Text(
                    "Compartir reporte",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                val textoReporte = o.generarResumen(
                    nombreCliente = cliente?.nombre ?: "Cliente",
                    tipoEquipo = equipo?.let {
                        "${it.marca} ${it.modelo}"
                    } ?: "Equipo"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            cliente?.email?.let { email ->
                                context.enviarEmail(
                                    email = email,
                                    asunto = "Reporte de servicio técnico #${o.id}",
                                    cuerpo = textoReporte
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = cliente?.email?.isNotBlank() == true
                    ) {
                        Icon(Icons.Default.Email, null)
                        Spacer(Modifier.width(4.dp))
                        Text("Email")
                    }

                    OutlinedButton(
                        onClick = { context.compartirOrden(textoReporte) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Share, null)
                        Spacer(Modifier.width(4.dp))
                        Text("Compartir")
                    }
                }
            }
        }
    }
}

@Composable
private fun SeccionReporte(titulo: String, contenido: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                contenido,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
