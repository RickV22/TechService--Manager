package com.example.proyecto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto.ui.viewmodel.ClienteViewModel
import com.example.proyecto.ui.viewmodel.ConfiguracionViewModel
import com.example.proyecto.ui.viewmodel.OrdenServicioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    clienteVM: ClienteViewModel,
    ordenVM: OrdenServicioViewModel,
    configVM: ConfiguracionViewModel,
    onNavigateToClientes: () -> Unit,
    onNavigateToConfig: () -> Unit
) {

    val prefs         by configVM.preferencias.collectAsState()
    val totalClientes by clienteVM.totalClientes.collectAsState()
    val pendientes    by ordenVM.pendientes.collectAsState()
    val enProceso     by ordenVM.enProceso.collectAsState()
    val finalizados   by ordenVM.finalizados.collectAsState()
    val ingresos      by ordenVM.totalIngresos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TechService Manager") },
                actions = {
                    ////btn configuracion
                    IconButton(onClick = onNavigateToConfig) {
                        Icon(Icons.Default.Settings, "Configuración")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
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

            Text(
                text = "Bienvenido, ${prefs.nombreTecnico} 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Resumen general",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            ////Fila con 2 tarjetas de estadísticas
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Clientes",
                    valor = totalClientes.toString(),
                    icon = Icons.Default.Person
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Pendientes",
                    valor = pendientes.toString(),
                    icon = Icons.Default.HourglassEmpty
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    titulo = "En proceso",
                    valor = enProceso.toString(),
                    icon = Icons.Default.Build
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    titulo = "Finalizados",
                    valor = finalizados.toString(),
                    icon = Icons.Default.CheckCircle
                )
            }

            ////Tarjeta de ingresos totales
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.AttachMoney,
                        null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            "Ingresos totales (finalizados)",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            "${prefs.moneda} %.2f".format(ingresos),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            HorizontalDivider()

            Text(
                "Accesos rápidos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            ////Botón para ir a la lista de clientes
            Button(
                onClick = onNavigateToClientes,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Default.People, null)
                Spacer(Modifier.width(8.dp))
                Text("Ver todos los clientes")
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    titulo: String,
    valor: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(
                valor,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    Text("Vista previa del Dashboard")
}