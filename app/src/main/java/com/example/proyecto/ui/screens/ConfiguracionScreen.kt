package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto.ui.viewmodel.ConfiguracionViewModel
import com.example.proyecto.utils.Constants
import com.example.proyecto.data.local.UserPreferences
import com.example.proyecto.ui.theme.ProyectoTheme

@Composable
fun ConfiguracionScreen(
    viewModel: ConfiguracionViewModel,
    onBack: () -> Unit
) {
    val prefs by viewModel.preferencias.collectAsState()

    ConfiguracionScreenContent(
        prefs = prefs,
        onNombreTecnicoChange = viewModel::setNombreTecnico,
        onTemaOscuroChange = viewModel::setTemaOscuro,
        onMonedaChange = viewModel::setMoneda,
        onOrdenRegistrosChange = viewModel::setOrdenRegistros,
        onNotificacionesChange = viewModel::setNotificaciones,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreenContent(
    prefs: UserPreferences,
    onNombreTecnicoChange: (String) -> Unit,
    onTemaOscuroChange: (Boolean) -> Unit,
    onMonedaChange: (String) -> Unit,
    onOrdenRegistrosChange: (String) -> Unit,
    onNotificacionesChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    var nombreLocal by remember(prefs.nombreTecnico) {
        mutableStateOf(prefs.nombreTecnico)
    }
    var menuMonedaAbierto by remember { mutableStateOf(false) }
    var menuOrdenAbierto  by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TituloSeccion("Perfil del técnico")

            OutlinedTextField(
                value = nombreLocal,
                onValueChange = { nombreLocal = it },
                label = { Text("Nombre del técnico") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                ////Btn guardar nombre
                trailingIcon = {
                    IconButton(
                        onClick = { onNombreTecnicoChange(nombreLocal) }
                    ) {
                        Icon(
                            Icons.Default.Check,
                            "Guardar nombre",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            TituloSeccion("Apariencia")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (prefs.temaOscuro) Icons.Default.DarkMode
                        else Icons.Default.LightMode,
                        null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Modo oscuro")
                }
                ////cambia el tema
                Switch(
                    checked = prefs.temaOscuro,
                    onCheckedChange = { onTemaOscuroChange(it) }
                )
            }

            // Menú para seleccionar la moneda
            ExposedDropdownMenuBox(
                expanded = menuMonedaAbierto,
                onExpandedChange = { menuMonedaAbierto = it }
            ) {
                OutlinedTextField(
                    value = prefs.moneda,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Moneda predeterminada") },
                    leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(menuMonedaAbierto)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = menuMonedaAbierto,
                    onDismissRequest = { menuMonedaAbierto = false }
                ) {
                    Constants.MONEDAS.forEach { moneda ->
                        DropdownMenuItem(
                            text = { Text(moneda) },
                            onClick = {
                                onMonedaChange(moneda)
                                menuMonedaAbierto = false
                            }
                        )
                    }
                }
            }

            ////registros
            TituloSeccion("Registros")

            ExposedDropdownMenuBox(
                expanded = menuOrdenAbierto,
                onExpandedChange = { menuOrdenAbierto = it }
            ) {
                OutlinedTextField(
                    value = Constants.ORDENES_VISUALIZACION
                        .find { it.first == prefs.ordenRegistros }
                        ?.second ?: prefs.ordenRegistros,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Orden de visualización") },
                    leadingIcon = { Icon(Icons.Default.Sort, null) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(menuOrdenAbierto)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = menuOrdenAbierto,
                    onDismissRequest = { menuOrdenAbierto = false }
                ) {
                    Constants.ORDENES_VISUALIZACION.forEach { (valor, etiqueta) ->
                        DropdownMenuItem(
                            text = { Text(etiqueta) },
                            onClick = {
                                onOrdenRegistrosChange(valor)
                                menuOrdenAbierto = false
                            }
                        )
                    }
                }
            }

            ///notificaciones
            TituloSeccion("Notificaciones")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Notificaciones activas")
                }
                Switch(
                    checked = prefs.notificaciones,
                    onCheckedChange = { onNotificacionesChange(it) }
                )
            }
        }
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Preview(showBackground = true)
@Composable
fun ConfiguracionScreenPreview() {
    ProyectoTheme {
        ConfiguracionScreenContent(
            prefs = UserPreferences(
                nombreTecnico = "Juan Pérez",
                temaOscuro = false,
                moneda = "USD",
                notificaciones = true,
                ordenRegistros = "DESC"
            ),
            onNombreTecnicoChange = {},
            onTemaOscuroChange = {},
            onMonedaChange = {},
            onOrdenRegistrosChange = {},
            onNotificacionesChange = {},
            onBack = {}
        )
    }
}
