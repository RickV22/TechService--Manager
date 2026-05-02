package com.example.proyecto.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto.ui.theme.*
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

    var showContent by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { showContent = true }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("CONSOLE", 
                        fontWeight = FontWeight.ExtraBold, 
                        letterSpacing = 2.sp,
                        style = MaterialTheme.typography.labelLarge
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(tween(600, easing = EaseInOutQuart)) + slideInVertically(tween(600)) { 20 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header section with Profile triggering config
                HeaderBento(
                    nombre = prefs.nombreTecnico,
                    onProfileClick = onNavigateToConfig
                )

                Text("OPERATIONAL OVERVIEW", 
                    style = MaterialTheme.typography.labelSmall, 
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Standardized Grid (Equal sizes)
                OverviewGrid(
                    clientes = totalClientes,
                    pendientes = pendientes,
                    enProceso = enProceso,
                    finalizados = finalizados
                )

                ModernIncomeCard(ingresos, prefs.moneda)

                QuickAccessSection(onNavigateToClientes)
            }
        }
    }
}

@Composable
private fun HeaderBento(nombre: String, onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Welcome back,", 
                style = MaterialTheme.typography.bodySmall, 
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(nombre, 
                style = MaterialTheme.typography.headlineMedium, 
                fontWeight = FontWeight.Black
            )
        }
        
        // Profile Circle acts as Settings/Config button
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(PremiumGradient))
                .clickable(onClick = onProfileClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nombre.take(1).uppercase(), 
                color = Color.White, 
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
            // Small overlay icon to hint at settings
            Icon(
                Icons.Rounded.Settings, 
                null, 
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .padding(2.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun OverviewGrid(clientes: Int, pendientes: Int, enProceso: Int, finalizados: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BentoItem(
                modifier = Modifier.weight(1f),
                label = "Clients",
                value = clientes.toString(),
                icon = Icons.Rounded.Groups,
                color = MaterialTheme.colorScheme.primary
            )
            BentoItem(
                modifier = Modifier.weight(1f),
                label = "Pending",
                value = pendientes.toString(),
                icon = Icons.Rounded.Analytics,
                color = Color(0xFFF59E0B)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BentoItem(
                modifier = Modifier.weight(1f),
                label = "Active",
                value = enProceso.toString(),
                icon = Icons.Rounded.Sync,
                color = Color(0xFF3B82F6)
            )
            BentoItem(
                modifier = Modifier.weight(1f),
                label = "Done",
                value = finalizados.toString(),
                icon = Icons.Rounded.CheckCircle,
                color = Color(0xFF10B981)
            )
        }
    }
}

@Composable
private fun BentoItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, null, modifier = Modifier.size(20.dp), tint = color)
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun ModernIncomeCard(monto: Double, moneda: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.03f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(52.dp).background(Brush.linearGradient(PremiumGradient), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.AccountBalanceWallet, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Column {
                Text("TOTAL REVENUE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$moneda %.2f".format(monto), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun QuickAccessSection(onNavigate: () -> Unit) {
    Button(
        onClick = onNavigate,
        modifier = Modifier.fillMaxWidth().height(70.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("OPEN CUSTOMER DIRECTORY", fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
            Icon(Icons.Rounded.ChevronRight, null)
        }
    }
}
