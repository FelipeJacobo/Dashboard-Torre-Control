# 📊 Dashboard Móvil — Torre de Control en Cobranza

![Android](https://img.shields.io/badge/Android-Kotlin-green?logo=android)
![Versión](https://img.shields.io/badge/Versión-1.0-blue)
![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-yellow)
![Arquitectura](https://img.shields.io/badge/Arquitectura-MVVM-purple)

Aplicación móvil desarrollada en **Kotlin** con **Jetpack Compose** para **Coppel**, que centraliza en tiempo real los **indicadores clave (KPIs)** de la operación de cobranza, optimizando la supervisión y reduciendo los tiempos de respuesta ante incidencias.  
Su objetivo es ofrecer una herramienta móvil intuitiva para que supervisores y gestores accedan rápidamente a métricas críticas de desempeño.

---

## 🧭 Contexto del Proyecto

Coppel, empresa del sector retail y financiero, maneja diariamente miles de operaciones de cobranza.  
Actualmente, la información sobre indicadores y alertas se encuentra dispersa en distintas plataformas, lo que retrasa la toma de decisiones y dificulta la gestión de incidencias.

👉 **Oportunidad:** Centralizar la información de cobranza en un solo dashboard móvil con actualizaciones automáticas, métricas visuales y accesibilidad desde cualquier lugar.

---

## 🚀 Estado Actual del Proyecto

### ✅ FUNCIONALIDADES IMPLEMENTADAS
- Sistema de **autenticación con roles** (Administrador / Usuario)
- Base de datos local con **Room** y **SQLite**
- **Dashboard principal** con visualización de KPIs en tiempo real
- Gráficas interactivas con **Vico Charts**
- Gestión de **incidencias** con CRUD completo
- **Navegación dinámica** con Jetpack Navigation Compose
- **Actualización automática** de KPIs cada 15 segundos con datos simulados

### 🛠️ EN DESARROLLO
- Optimización de rendimiento en gráficas y base de datos  
- Pruebas unitarias e instrumentadas  
- Ajustes visuales en temas y tipografía (modo claro/oscuro)  

---

## 🏗️ Arquitectura del Proyecto

### 📁 Estructura de carpetas

📱 Dashboard-Torre-Control/
├── app/
│ ├── src/main/java/com/example/dashboardcobranza/
│ │ ├── data/
│ │ │ ├── database/ # Entidades y DAOs de Room
│ │ │ ├── repository/ # Repositorios de acceso a datos
│ │ │ └── model/ # Modelos de dominio (KPI, User, Issue)
│ │ ├── domain/
│ │ │ └── usecases/ # Lógica de negocio (casos de uso)
│ │ ├── presentation/
│ │ │ ├── ui/
│ │ │ │ ├── components/ # Componentes reutilizables de Compose
│ │ │ │ ├── theme/ # Colores, tipografías y estilos
│ │ │ │ ├── login/ # Pantalla de autenticación
│ │ │ │ ├── dashboard/ # Pantalla principal de KPIs
│ │ │ │ ├── issues/ # Módulo de incidencias
│ │ │ │ └── profile/ # Perfil de usuario y configuración
│ │ │ └── viewmodel/ # ViewModels con StateFlow
│ │ └── navigation/ # Rutas y navegación de Compose
├── build.gradle.kts
└── gradle.properties

### 🧩 Patrón Arquitectónico
**MVVM + Clean Architecture**


---

## ⚙️ Stack Tecnológico

| Categoría | Tecnologías |
|------------|--------------|
| 💙 **Lenguaje & Framework** | Kotlin, Jetpack Compose, Material Design 3 |
| 🏗️ **Arquitectura & Patrones** | MVVM, Clean Architecture, StateFlow |
| 💾 **Persistencia de Datos** | Room, DataStore, SQLite |
| 🔄 **Programación Asíncrona** | Coroutines, Flow |
| 📊 **Visualización de Datos** | Vico Charts, Canvas de Compose |
| 🧭 **Navegación & DI** | Navigation Compose, Hilt/Dagger |

---

## 📈 KPIs Implementados

| Indicador | Estado | Tecnología | Frecuencia |
|------------|---------|-------------|-------------|
| 💰 **Eficiencia de Cobranza** | ✅ Implementado | Vico Line Chart | Tiempo real |
| ⏱️ **Tiempo de Resolución de Incidencias** | ✅ Implementado | Room + Compose | Cada 15 seg |
| 👥 **Clientes Gestionados por Agente** | ✅ Implementado | Bar Charts | Tiempo real |
| 📉 **Nivel de Morosidad** | ✅ Implementado | Pie Charts | Cada 30 seg |
| 🎯 **Cumplimiento de Metas de Atención** | ✅ Implementado | Progress Bars | Tiempo real |

---

## 🔐 Sistema de Roles

### 👨‍💼 Administrador
- Acceso completo a todas las funciones  
- Gestión de usuarios y permisos  
- Edición de KPIs y configuraciones  
- Generación de reportes detallados  

### 👥 Usuario Estándar
- Visualización de dashboards e indicadores  
- Consulta de KPIs asignados  
- Registro y seguimiento de incidencias  
- Acceso limitado a configuraciones  

---

## 📱 Pantallas Principales

1. **Inicio de Sesión** → Acceso con número de empleado o cuenta Google  
2. **Dashboard Principal** → KPIs con semaforización (verde/amarillo/rojo)  
3. **Gestión de Incidencias** → CRUD completo con categorías  
4. **Alertas y Notificaciones** → Tarjetas dinámicas de prioridad  
5. **Perfil y Configuración** → Datos personales y preferencias  
6. **Modo Oscuro/Claro** → Activación automática según el sistema  
7. **Resumen General** → Visualización compacta de rendimiento

---

## 🧩 Ejemplo Visual (Mockup)

| Login | Dashboard | Incidencias | Perfil |
|:------:|:-----------:|:------------:|:---------:|
| ![Login](https://via.placeholder.com/150) | ![Dashboard](https://via.placeholder.com/150) | ![Incidencias](https://via.placeholder.com/150) | ![Perfil](https://via.placeholder.com/150) |

---

## 🚀 Cómo Ejecutar el Proyecto

```bash
# 1. Clonar el repositorio
git clone https://github.com/FelipeJacobo/Dashboard-Torre-Control.git

# 2. Abrir en Android Studio
# 3. Sincronizar el proyecto con archivos Gradle
# 4. Ejecutar en emulador o dispositivo físico

🧾 Requisitos

Android Studio Flamingo o superior

SDK Android API 21+

Dispositivo o emulador con Android 5.0+

🐛 Solución de Problemas Recientes
✅ CORREGIDOS

Gráfica de Vico Charts: rendimiento optimizado y actualización fluida

Botón Editar: corregido en modo administrador

Base de datos: mejoradas relaciones y entidades

Navegación: transiciones corregidas entre pantallas

🔧 EN PROCESO

Optimización de consumo de memoria

Mejoras en la actualización automática de KPIs

Pruebas de estrés con grandes volúmenes de datos

🧠 Futuras Mejoras

Integración con servicios REST (API Flask o Node.js)

Módulo de predicción de morosidad con IA básica

Exportación de reportes en PDF

Modo supervisor con estadísticas de equipo


📞 Contacto

👤 Felipe Jacobo
🔗 GitHub - FelipeJacobo

📘 Proyecto: Dashboard-Torre-Control
