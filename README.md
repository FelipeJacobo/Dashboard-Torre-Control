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
- Seguimiento de incidencias  
- Acceso limitado a configuraciones  

---

## 📱 Pantallas Principales

1. **Inicio de Sesión** → Autenticación con número de empleado o cuenta Google  
2. **Registro de Usuario** → Creación de nueva cuenta con credenciales  
3. **Dashboard Principal** → Vista general de KPIs con semaforización (verde/amarillo/rojo)  
4. **Detalle de KPI** → Análisis específico de cada indicador con métricas detalladas  
5. **Gestión de Incidencias** → CRUD completo con categorías y prioridades  
6. **Detalle de Incidencia** → Información específica y seguimiento de cada caso  
7. **Perfil de Usuario** → Datos personales, estadísticas y preferencias  
8. **Configuración** → Personalización de alertas, tema y notificaciones


---
## 🧩 Ejemplo Visual

| Login | Dashboard | Incidencias | Perfil | Configuración |
|:------:|:-----------:|:------------:|:---------:|:--------------:|
| ![Login](./Screenshots/LOGIN.png) | ![Dashboard](./Screenshots/DASHBOARD.png) | ![Incidencias](./Screenshots/INCIDENCIAS.png) | ![Perfil](./Screenshots/PERFIL.png) | ![Configuración](./Screenshots/CONFIGURACION.png) |

---

## 📲 Descargar APK

### 🎯 **Versión Actual**
[![Descargar APK](https://img.shields.io/badge/Descargar-APK_Debug-green?style=for-the-badge&logo=android)](https://github.com/FelipeJacobo/Dashboard-Torre-Control/raw/main/app/build/outputs/apk/debug/app-debug.apk)

## 🧾 Requisitos del Sistema

### 🛠️ Para Desarrollo
- **Android Studio Flamingo** o superior
- **SDK Android API 21+** (Android 5.0 Lollipop)
- **Kotlin 1.9+** 
- **Gradle 8.0+**

### 📱 Para Ejecución
- **Dispositivo o emulador** con Android 5.0+
- **RAM mínima:** 2GB
- **Almacenamiento:** 50MB libres
- **Conexión:** Internet para datos en tiempo real

---

## 🐛 Solución de Problemas Recientes

### ✅ CORREGIDOS
- **Gráficas Vico Charts** - Rendimiento optimizado y actualización fluida
- **Botón Editar** - Comportamiento corregido en modo administrador  
- **Base de Datos** - Relaciones y entidades mejoradas
- **Navegación** - Transiciones corregidas entre pantallas
- **Autenticación** - Validación de roles mejorada

### 🔧 EN PROCESO
- **Optimización de memoria** - Reducción del consumo de RAM
- **Actualización automática** - Mejoras en la sincronización de KPIs
- **Pruebas de estrés** - Validación con grandes volúmenes de datos
- **Compatibilidad** - Soporte para más versiones de Android

---

## 🧠 Futuras Mejoras

### 🚀 Próximas Versiones
- **Integración REST** - API Flask o Node.js para datos en tiempo real
- **Exportación PDF** - Generación de reportes ejecutivos
- **Modo Supervisor** - Estadísticas detalladas por equipo
- **Notificaciones Push** - Alertas en tiempo real
- **Backup Cloud** - Sincronización con servicios en la nube
- **Multidioma** - Soporte para español e inglés

### 🎯 Roadmap 2025
- **Q4 2025** - Versión estable 1.0
- **Q1 2026** - Integración con APIs corporativas
- **Q2 2026** - Panel web complementario

---

## 📞 Contacto y Soporte

**👤 Desarrollador:** Felipe Jacobo  
**🔗 GitHub:** [FelipeJacobo](https://github.com/FelipeJacobo)  
**📘 Proyecto:** [Dashboard-Torre-Control](https://github.com/FelipeJacobo/Dashboard-Torre-Control)  
**🐛 Reportar Issues:** [GitHub Issues](https://github.com/FelipeJacobo/Dashboard-Torre-Control/issues)

### 💬 ¿Necesitas ayuda?
- **Documentación:** Revisa la carpeta `/docs`
- **Problemas técnicos:** Abre un issue en GitHub
- **Sugerencias:** Crea una discusión en el repositorio

---

**🕒 Última actualización:** 25 de octubre de 2025
