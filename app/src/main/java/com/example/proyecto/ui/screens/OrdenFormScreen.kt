package com.example.proyecto.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto.data.local.entity.EstadoServicio
import com.example.proyecto.data.local.entity.OrdenServicio
import com.example.proyecto.ui.theme.PremiumGradient
import com.example.proyecto.ui.viewmodel.OrdenServicioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenFormScreen(
    equipoId: Long,
    ordenId: Long?,
    viewModel: OrdenServicioViewModel,
    onGuardar: () -> Unit,
    onBack: () -> Unit
) {
    var problema       by remember { mutableStateOf("") }
    var diagnostico    by remember { mutableStateOf("") }
    var solucion       by remember { mutableStateOf("") }
    var costo          by remember { mutableStateOf("") }
    var estadoActual   by remember { mutableStateOf(EstadoServicio.PENDIENTE) }
    var fechaIngresoOriginal by remember { mutableLongStateOf(System.currentTimeMillis()) }

    var menuEstadoAbierto by remember { mutableStateOf(false) }
    var problemaVacio  by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { showContent = true }

    if (ordenId != null) {
        val flujoOrden = remember { viewModel.getOrdenById(ordenId) }
        val orden by flujoOrden.collectAsState(initial = null)

        LaunchedEffect(orden) {
            orden?.let {
                problema     = it.problemaReportado
                diagnostico  = it.diagnosticoTecnico
                solucion     = it.solucionAplicada
                costo        = if (it.costo > 0) it.costo.toString() else ""
                estadoActual = it.estado
                fechaIngresoOriginal = it.fechaIngreso
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (ordenId != null) "EDIT LOG" else "INITIALIZE LOG",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { 20 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    "SERVICE PARAMETERS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = problema,
                    onValueChange = {
                        problema = it
                        problemaVacio = false
                    },
                    label = { Text("Reported Issue *") },
                    placeholder = { Text("Describe the technical failure...") },
                    isError = problemaVacio,
                    supportingText = if (problemaVacio) {
                        { Text("Required field: analysis cannot proceed without data") }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    minLines = 3
                )

                OutlinedTextField(
                    value = diagnostico,
                    onValueChange = { diagnostico = it },
                    label = { Text("Technical Diagnosis") },
                    placeholder = { Text("Root cause analysis results...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    minLines = 3
                )

                OutlinedTextField(
                    value = solucion,
                    onValueChange = { solucion = it },
                    label = { Text("Applied Solution") },
                    placeholder = { Text("Corrective actions performed...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    minLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = costo,
                        onValueChange = {
                            costo = it.filter { c -> c.isDigit() || c == '.' }
                        },
                        label = { Text("Service Cost") },
                        leadingIcon = { Icon(Icons.Rounded.Payments, null, modifier = Modifier.size(20.dp)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = menuEstadoAbierto,
                        onExpandedChange = { menuEstadoAbierto = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = when (estadoActual) {
                                EstadoServicio.PENDIENTE -> "PENDING"
                                EstadoServicio.EN_PROCESO -> "ACTIVE"
                                EstadoServicio.FINALIZADO -> "DONE"
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(menuEstadoAbierto) },
                            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = menuEstadoAbierto,
                            onDismissRequest = { menuEstadoAbierto = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("PENDING") },
                                onClick = {
                                    estadoActual = EstadoServicio.PENDIENTE
                                    menuEstadoAbierto = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("ACTIVE") },
                                onClick = {
                                    estadoActual = EstadoServicio.EN_PROCESO
                                    menuEstadoAbierto = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("DONE") },
                                onClick = {
                                    estadoActual = EstadoServicio.FINALIZADO
                                    menuEstadoAbierto = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        problemaVacio = problema.isBlank()
                        if (!problemaVacio) {
                            val orden = OrdenServicio(
                                id                 = ordenId ?: 0,
                                equipoId           = equipoId,
                                fechaIngreso       = if (ordenId != null) fechaIngresoOriginal else System.currentTimeMillis(),
                                problemaReportado  = problema.trim(),
                                diagnosticoTecnico = diagnostico.trim(),
                                solucionAplicada   = solucion.trim(),
                                costo              = costo.toDoubleOrNull() ?: 0.0,
                                estado             = estadoActual,
                                fechaActualizacion = System.currentTimeMillis()
                            )
                            if (ordenId != null) viewModel.updateOrden(orden)
                            else viewModel.insertOrden(orden)
                            onGuardar()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(PremiumGradient)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Save, null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(12.dp))
                            Text("SAVE TO DATABASE", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        }
                    }
                }
            }
        }
    }
}
