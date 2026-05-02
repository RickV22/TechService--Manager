package com.example.proyecto.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.EventNote
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val ordenAEliminarState = remember { mutableStateOf<OrdenServicio?>(null) }

    ordenAEliminarState.value?.let { orden ->
        DeleteConfirmDialog(
            titulo = "DE-AUTHORIZE ORDER",
            mensaje = "This operation will permanently purge order #${orden.id} from the records. Proceed?",
            onConfirm = {
                ordenVM.deleteOrden(orden)
                ordenAEliminarState.value = null
            },
            onDismiss = { ordenAEliminarState.value = null }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(equipo?.let { "${it.marca} ${it.modelo}" }?.uppercase() ?: "SERVICE LOGS",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text("SYSTEM RECORDS", 
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBackIosNew, null, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = alAgregarOrden,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Rounded.Add, "New Order")
            }
        }
    ) { padding ->
        if (listaOrdenes.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                EmptyState("Terminal ready. No service records found for this unit.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(listaOrdenes, key = { _, it -> it.id }) { index, orden ->
                    var isVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { isVisible = true }

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(500, delayMillis = index * 50)) + 
                                slideInHorizontally(tween(500, delayMillis = index * 50)) { -40 }
                    ) {
                        ModernOrderCard(
                            orden = orden,
                            onClick = { alVerOrden(orden.id) },
                            onDelete = { ordenAEliminarState.value = orden }
                        )
                    }
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
private fun ModernOrderCard(
    orden: OrdenServicio,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "ID: ${orden.id.toString().padStart(4, '0')}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Rounded.DeleteOutline, null, tint = Color.Red.copy(alpha = 0.4f), modifier = Modifier.size(20.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                orden.problemaReportado,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EstadoChip(orden.estado)
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Rounded.EventNote, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        orden.fechaIngreso.toFechaLegible(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
