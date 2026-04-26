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
import com.example.proyecto.data.local.entity.Cliente
import com.example.proyecto.ui.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteFormScreen(
    clienteId: Long?,
    viewModel: ClienteViewModel,
    onSave: () -> Unit,
    onBack: () -> Unit
) {

    var nombre    by remember { mutableStateOf("") }
    var telefono  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    var nombreError   by remember { mutableStateOf(false) }
    var telefonoError by remember { mutableStateOf(false) }

    if (clienteId != null) {
        val clienteFlow = remember { viewModel.getClienteById(clienteId) }
        val cliente by clienteFlow.collectAsState(initial = null)

        LaunchedEffect(cliente) {
            cliente?.let {
                nombre    = it.nombre
                telefono  = it.telefono
                email     = it.email
                direccion = it.direccion
            }
        }
    }

    val titulo = if (clienteId != null) "Editar cliente" else "Nuevo cliente"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
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

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = false
                },
                label = { Text("Nombre completo *") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                isError = nombreError,
                supportingText = if (nombreError) {
                    { Text("El nombre es obligatorio") }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    telefonoError = false
                },
                label = { Text("Teléfono *") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = telefonoError,
                supportingText = if (telefonoError) {
                    { Text("El teléfono es obligatorio") }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(Modifier.height(8.dp))

            /////guardar
            Button(
                onClick = {
                    nombreError   = nombre.isBlank()
                    telefonoError = telefono.isBlank()

                    if (!nombreError && !telefonoError) {
                        val cliente = Cliente(
                            id        = clienteId ?: 0,
                            nombre    = nombre.trim(),
                            telefono  = telefono.trim(),
                            email     = email.trim(),
                            direccion = direccion.trim()
                        )
                        if (clienteId != null) viewModel.updateCliente(cliente)
                        else viewModel.insertCliente(cliente)

                        onSave()
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