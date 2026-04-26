package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.local.entity.Equipo
import com.example.proyecto.ui.viewmodel.EquipoViewModel
import com.example.proyecto.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipoFormScreen(
    clienteId: Long,
    equipoId: Long?,
    viewModel: EquipoViewModel,
    onGuardar: () -> Unit,
    onBack: () -> Unit
) {
    ////formulario
    var tipoSeleccionado  by remember { mutableStateOf(Constants.TIPOS_EQUIPO.first()) }
    var marca             by remember { mutableStateOf("") }
    var modelo            by remember { mutableStateOf("") }
    var numeroSerie       by remember { mutableStateOf("") }
    var estadoInicial     by remember { mutableStateOf("") }

    var menuTipoAbierto   by remember { mutableStateOf(false) }
    var marcaVacia        by remember { mutableStateOf(false) }
    var modeloVacio       by remember { mutableStateOf(false) }

    if (equipoId != null) {
        val flujoEquipo = remember { viewModel.getEquipoById(equipoId) }
        val equipo by flujoEquipo.collectAsState(initial = null)

        LaunchedEffect(equipo) {
            equipo?.let {
                tipoSeleccionado = it.tipo
                marca            = it.marca
                modelo           = it.modelo
                numeroSerie      = it.numeroSerie
                estadoInicial    = it.estadoInicial
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (equipoId != null) "Editar equipo" else "Nuevo equipo")
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

            ////menu desplegable--Constants.kt
            ExposedDropdownMenuBox(
                expanded = menuTipoAbierto,
                onExpandedChange = { menuTipoAbierto = it }
            ) {
                OutlinedTextField(
                    value = tipoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de equipo *") },
                    leadingIcon = { Icon(Icons.Default.Devices, null) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(menuTipoAbierto)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = menuTipoAbierto,
                    onDismissRequest = { menuTipoAbierto = false }
                ) {
                    // Creamos una opción por cada tipo en la lista
                    Constants.TIPOS_EQUIPO.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                tipoSeleccionado = tipo
                                menuTipoAbierto = false
                            }
                        )
                    }
                }
            }
            ////obligatirio
            OutlinedTextField(
                value = marca,
                onValueChange = {
                    marca = it
                    marcaVacia = false
                },
                label = { Text("Marca *") },
                isError = marcaVacia,
                supportingText = if (marcaVacia) {
                    { Text("La marca es obligatoria") }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = modelo,
                onValueChange = {
                    modelo = it
                    modeloVacio = false
                },
                label = { Text("Modelo *") },
                isError = modeloVacio,
                supportingText = if (modeloVacio) {
                    { Text("El modelo es obligatorio") }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ////opcional
            OutlinedTextField(
                value = numeroSerie,
                onValueChange = { numeroSerie = it },
                label = { Text("Número de serie") },
                leadingIcon = { Icon(Icons.Default.QrCode, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            //////para describir el estado físico del equipo
            OutlinedTextField(
                value = estadoInicial,
                onValueChange = { estadoInicial = it },
                label = { Text("Estado inicial del equipo") },
                placeholder = {
                    Text("Ej: Pantalla rayada, teclado con tecla F rota...")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    ////validacion de campos obligatorios
                    marcaVacia  = marca.isBlank()
                    modeloVacio = modelo.isBlank()

                    if (!marcaVacia && !modeloVacio) {
                        val equipo = Equipo(
                            id            = equipoId ?: 0,
                            clienteId     = clienteId,
                            tipo          = tipoSeleccionado,
                            marca         = marca.trim(),
                            modelo        = modelo.trim(),
                            numeroSerie   = numeroSerie.trim(),
                            estadoInicial = estadoInicial.trim()
                        )
                        if (equipoId != null) viewModel.updateEquipo(equipo)
                        else viewModel.insertEquipo(equipo)

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