package com.example.proyecto.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto.data.local.entity.Cliente
import com.example.proyecto.ui.components.EmptyState
import com.example.proyecto.ui.theme.PremiumGradient
import com.example.proyecto.ui.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteListScreen(
    viewModel: ClienteViewModel,
    onClienteClick: (Long) -> Unit,
    onAddCliente: () -> Unit,
    onBack: () -> Unit
) {
    val clientes    by viewModel.clientes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("CLIENT DIRECTORY", 
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBackIosNew, null, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = onAddCliente,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Rounded.Add, "Add", modifier = Modifier.size(32.dp))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            SearchBarModern(searchQuery, viewModel::setSearchQuery)

            if (clientes.isEmpty()) {
                EmptyState("No matching clients found in the secure database.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(clientes, key = { _, it -> it.id }) { index, cliente ->
                        var isVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { isVisible = true }

                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn(tween(600, delayMillis = index * 40)) + 
                                    slideInVertically(tween(600, delayMillis = index * 40)) { 20 }
                        ) {
                            ModernClienteCard(
                                cliente = cliente,
                                onClick = { onClienteClick(cliente.id) }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SearchBarModern(query: String, onQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Filter by identity or contact...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Rounded.Search, null, tint = MaterialTheme.colorScheme.primary) },
            shape = RoundedCornerShape(20.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

@Composable
private fun ModernClienteCard(cliente: Cliente, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(52.dp).background(Brush.linearGradient(PremiumGradient), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(cliente.nombre.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(cliente.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Rounded.Phone, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(6.dp))
                    Text(cliente.telefono, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.05f)
            ) {
                Icon(Icons.Rounded.ChevronRight, null, modifier = Modifier.padding(8.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
