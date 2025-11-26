# Ahorros App - Sistema de Gestión de Planes de Ahorro

## 📋 Descripción General
Esta aplicación Android permite gestionar planes de ahorro grupales. Los usuarios pueden ver una lista de planes, consultar los detalles de cada plan (miembros, pagos, progreso), crear nuevos planes y registrar pagos.

## 🏗️ Arquitectura y Diseño Técnico

El proyecto sigue la arquitectura **MVVM (Model-View-ViewModel)** recomendada por Google para asegurar un código modular, testeable y fácil de mantener.

### 1. Patrón MVVM (Model-View-ViewModel)
- **View (Activity/Fragment)**: Se encarga únicamente de mostrar la interfaz de usuario y observar los cambios en el estado. No contiene lógica de negocio.
- **ViewModel**: Mantiene el estado de la UI (`UiState`) y sobrevive a los cambios de configuración. Se comunica con los repositorios para obtener datos y expone `LiveData` para que la vista reaccione.
- **Model**: Representa los datos y la lógica de negocio.

**Justificación**: MVVM permite separar la lógica de presentación de la lógica de negocio, lo que facilita las pruebas unitarias y hace que el código sea más limpio.

### 2. Patrón Repository
Se implementó el patrón Repository (`PlansRepository`, `MembersRepository`, `PaymentsRepository`) para actuar como una única fuente de verdad.
- Abstrae el origen de los datos (en este caso, la API REST).
- Permite cambiar la implementación de datos (ej. añadir base de datos local Room) sin afectar al ViewModel.

### 3. Retrofit para Networking
Se utilizó **Retrofit** para el consumo de la API REST.
- **Ventajas**: Manejo sencillo de peticiones HTTP, conversión automática de JSON a objetos Kotlin (usando Gson), y soporte para Coroutines.
- **Configuración**: Se usa un `RetrofitClient` singleton para mantener una única instancia y optimizar recursos.

### 4. Coroutines y LiveData
- **Coroutines**: Para realizar operaciones asíncronas (llamadas de red) sin bloquear el hilo principal.
- **LiveData**: Para comunicar los datos del ViewModel a la UI de manera reactiva y segura con el ciclo de vida.

## 🚀 Cómo Ejecutar el Proyecto

1. **Backend**: Asegúrate de que el servidor Node.js esté corriendo en el puerto 3000.
2. **Configuración de Red**:
   - La app está configurada para conectar a `http://10.0.2.2:3000/api/` (localhost del emulador Android).
   - Se ha habilitado `usesCleartextTraffic="true"` en el manifiesto para permitir conexiones HTTP locales.
3. **Compilación**: Abre el proyecto en Android Studio y haz clic en "Run".

## ✅ Entregables Implementados

- **Persona 1**: Listado de planes, configuración de Retrofit.
- **Persona 2**: Detalle de plan, lista de miembros, cálculo de progreso.
- **Persona 3**: Creación de planes, registro de pagos, navegación completa.
