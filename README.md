

# 🔧 TechService Manager

Aplicación Android para gestionar servicios técnicos de equipos electrónicos. Desarrollada como proyecto universitario usando las tecnologías modernas de Android.

---
<p>
    <img width="240" height="470" alt="1" src="https://github.com/user-attachments/assets/869ff92a-a4a4-483a-8653-3168b7b3d173" />
    <img width="240" height="470" alt="10" src="https://github.com/user-attachments/assets/41e5680d-eb18-4c1d-a5f7-fb6617cf2e04" />
    <img width="240" height="470" alt="2" src="https://github.com/user-attachments/assets/0d66f070-c92c-40e6-8881-cffe0fa59b63" />
</p>

---

##  Descripción

TechService Manager permite a un técnico en reparación de equipos llevar el control completo de su taller desde el celular. Puede registrar clientes, sus equipos y las órdenes de servicio de cada equipo, además de ver un resumen de todo en el Dashboard.

---

##  Funcionalidades

- **Gestión de clientes** — registrar, editar, eliminar y buscar clientes
- **Gestión de equipos** — cada cliente puede tener varios equipos registrados
- **Órdenes de servicio** — crear y hacer seguimiento a cada reparación
- **Dashboard** — resumen en tiempo real de pendientes, en proceso, finalizados e ingresos
- **Llamar al cliente** — desde el detalle del cliente con un toque
- **Abrir en Maps** — ver la dirección del cliente en Google Maps
- **Compartir reporte** — enviar el reporte por email, WhatsApp u otras apps
- **Modo oscuro** — cambia el tema desde la pantalla de configuración
- **Preferencias persistentes** — el nombre del técnico, moneda y configuración se guardan aunque cierres la app

---

##  Tecnologías usadas

| Tecnología | Para qué se usa |
|---|---|
| Kotlin | Lenguaje principal |
| Jetpack Compose | Construcción de la interfaz |
| Material Design 3 | Estilos y componentes visuales |
| Room (SQLite) | Base de datos local |
| DataStore | Guardar preferencias del usuario |
| Navigation Compose | Navegación entre pantallas |
| ViewModel + StateFlow | Arquitectura MVVM |
| Coroutines + Flow | Manejo de datos en segundo plano |

---

##  Arquitectura

El proyecto sigue el patrón **MVVM** (Model - View - ViewModel):

```
UI (Pantallas)
    ↕
ViewModel
    ↕
Repository
    ↕
DAO
    ↕
Room (SQLite)
```

---

## 📁 Estructura del proyecto

```
com.example.proyecto
├── data
│   ├── local
│   │   ├── entity          ← Tablas de la base de datos
│   │   │   ├── Cliente.kt
│   │   │   ├── Equipo.kt
│   │   │   └── OrdenServicio.kt
│   │   ├── dao             ← Consultas a la base de datos
│   │   │   ├── ClienteDao.kt
│   │   │   ├── EquipoDao.kt
│   │   │   └── OrdenServicioDao.kt
│   │   ├── AppDatabase.kt
│   │   └── PreferencesManager.kt
│   └── repository          ← Capa intermedia entre ViewModel y DAO
│       ├── ClienteRepository.kt
│       ├── EquipoRepository.kt
│       └── OrdenServicioRepository.kt
├── ui
│   ├── screens             ← Las 10 pantallas de la app
│   ├── viewmodel           ← Los 4 ViewModels
│   ├── components          ← Componentes reutilizables
│   └── theme               ← Colores y estilos
├── navigation
│   ├── Screen.kt           ← Rutas de navegación
│   └── AppNavigation.kt    ← Mapa de pantallas
├── utils
│   ├── Constants.kt        ← Listas y constantes
│   └── Extensions.kt       ← Intents implícitos y helpers
├── App.kt                  ← Inicialización de la app
└── MainActivity.kt         ← Punto de entrada
```

---

##  Modelo de datos

```
Cliente (1) ──── (N) Equipo (1) ──── (N) OrdenServicio
```

Un cliente puede tener varios equipos y cada equipo puede tener varias órdenes de servicio. Si se elimina un cliente, se eliminan automáticamente sus equipos y órdenes.

---

## 🚀 Cómo correr el proyecto

1. Clona el repositorio
```bash
git clone https://github.com/tu-usuario/techservice-manager.git
```

2. Abre el proyecto con **Android Studio Hedgehog** o posterior

3. Espera que Gradle sincronice las dependencias

4. Corre la app en un emulador o dispositivo físico con **Android 7.0 (API 24)** o superior

---

## temas

- Cómo estructurar un proyecto Android con arquitectura MVVM
- Crear y relacionar tablas con Room (SQLite)
- Guardar preferencias del usuario con DataStore
- Navegación entre pantallas con argumentos usando Navigation Compose
- Comunicación entre apps con Intents implícitos
- Manejo de datos reactivos con Flow y StateFlow
- Construcción de interfaces con Jetpack Compose y Material Design 3

---

## 👤 Autor

**Ricardo Valiente**
Estudiante de Ingeniería Telemática

---

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos.
