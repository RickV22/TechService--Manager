package com.example.proyecto.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto.data.local.entity.Equipo
import com.example.proyecto.ui.components.DeleteConfirmDialog
import com.example.proyecto.ui.components.EmptyState
import com.example.proyecto.ui.theme.PremiumGradient
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
    val equipoAEliminarState = remember { mutableStateOf<Equipo?>(null) }

    equipoAEliminarState.value?.let { equipo ->
        DeleteConfirmDialog(
            titulo = "DECOMMISSION EQUIPMENT",
            mensaje = "Remove ${equipo.marca} ${equipo.modelo}? All associated service logs will be purged.",
            onConfirm = {
                viewModel.deleteEquipo(equipo)
                equipoAEliminarState.value = null
            },
            onDismiss = {
                equipoAEliminarState.value = null
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("HARDWARE ASSETS", 
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = onAgregarEquipo,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Rounded.Add, "Add Asset", modifier = Modifier.size(32.dp))
            }
        }
    ) { padding ->

        if (listaEquipos.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                EmptyState("No assets registered in terminal database.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(listaEquipos, key = { _, it -> it.id }) { index, equipo ->
                    var isVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { isVisible = true }

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(500, delayMillis = index * 40)) + 
                                slideInVertically(tween(500, delayMillis = index * 40)) { 20 }
                    ) {
                        FilaEquipo(
                            equipo = equipo,
                            alHacerClick = { onEquipoClick(equipo.id) },
                            alEditarClick = { onEditarEquipo(equipo.id) },
                            alEliminarClick = { equipoAEliminarState.value = equipo }
                        )
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = alHacerClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(Brush.linearGradient(PremiumGradient), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Devices,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${equipo.marca} ${equipo.modelo}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${equipo.tipo} • S/N: ${equipo.numeroSerie}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = alEditarClick, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Rounded.Edit,
                        null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = alEliminarClick, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Rounded.DeleteOutline,
                        null,
                        tint = Color.Red.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
