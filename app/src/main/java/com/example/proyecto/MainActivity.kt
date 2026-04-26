package com.example.proyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.proyecto.navigation.AppNavigation
import com.example.proyecto.ui.theme.ProyectoTheme
import com.example.proyecto.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    private val app: App by lazy { application as App }

        private val clienteVM: ClienteViewModel by viewModels {
        ClienteViewModelFactory(app.clienteRepository)
    }
    private val equipoVM: EquipoViewModel by viewModels {
        EquipoViewModelFactory(app.equipoRepository)
    }
    private val ordenVM: OrdenServicioViewModel by viewModels {
        OrdenServicioViewModelFactory(app.ordenServicioRepository)
    }
    private val configVM: ConfiguracionViewModel by viewModels {
        ConfiguracionViewModelFactory(app.preferencesManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val prefs by configVM.preferencias.collectAsState()
            val navController = rememberNavController()

            ProyectoTheme(darkTheme = prefs.temaOscuro) {
                AppNavigation(
                    navController = navController,
                    clienteVM     = clienteVM,
                    equipoVM      = equipoVM,
                    ordenVM       = ordenVM,
                    configVM      = configVM
                )
            }
        }
    }
}