package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.local.entity.EstadoServicio
import com.example.proyecto.data.local.entity.OrdenServicio
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
        topBar = {
            TopAppBar(
                title = {
                    Text(if (ordenId != null) "Editar orden" else "Nueva orden")
                },
                navigationIcon = {
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ////obligatorio
            OutlinedTextField(
                value = problema,
                onValueChange = {
                    problema = it
                    problemaVacio = false
                },
                label = { Text("Problema reportado *") },
                placeholder = {
                    Text("Ej: El equipo no enciende, pantalla parpadeante...")
                },
                isError = problemaVacio,
                supportingText = if (problemaVacio) {
                    { Text("Debes describir el problema") }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            /////diagnóstico opcional al principio, se llena despues de revisar el equipo
            OutlinedTextField(
                value = diagnostico,
                onValueChange = { diagnostico = it },
                label = { Text("Diagnóstico técnico") },
                placeholder = {
                    Text("Ej: Falla en la fuente de poder, RAM dañada...")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            ////solución-opcional al principio, se llena cuando termina el servicio
            OutlinedTextField(
                value = solucion,
                onValueChange = { solucion = it },
                label = { Text("Solución aplicada") },
                placeholder = {
                    Text("Ej: Se reemplazó la fuente de poder, se instaló RAM nueva...")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = costo,
                onValueChange = {
                    costo = it.filter { caracter ->
                        caracter.isDigit() || caracter == '.'
                    }
                },
                label = { Text("Costo del servicio") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ////menu desplegable para el estado de la orden
            ExposedDropdownMenuBox(
                expanded = menuEstadoAbierto,
                onExpandedChange = { menuEstadoAbierto = it }
            ) {
                OutlinedTextField(
                    value = when (estadoActual) {
                        EstadoServicio.PENDIENTE  -> "Pendiente"
                        EstadoServicio.EN_PROCESO -> "En proceso"
                        EstadoServicio.FINALIZADO -> "Finalizado"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado del servicio") },
                    leadingIcon = { Icon(Icons.Default.Flag, null) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(menuEstadoAbierto)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = menuEstadoAbierto,
                    onDismissRequest = { menuEstadoAbierto = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Pendiente") },
                        onClick = {
                            estadoActual = EstadoServicio.PENDIENTE
                            menuEstadoAbierto = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("En proceso") },
                        onClick = {
                            estadoActual = EstadoServicio.EN_PROCESO
                            menuEstadoAbierto = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Finalizado") },
                        onClick = {
                            estadoActual = EstadoServicio.FINALIZADO
                            menuEstadoAbierto = false
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

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
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Default.Save, null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar")
            }
        }
    }
}