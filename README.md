# 🏎️ DriveN'Dodge - Android App & Unity Launcher

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Java-orange)
![Game Engine](https://img.shields.io/badge/Engine-Unity-black)
![Networking](https://img.shields.io/badge/Networking-Retrofit2-blue)
![Backend](https://img.shields.io/badge/Backend-GitHub-lightgrey)

> **DriveN'Dodge** es una aplicación híbrida que combina la gestión de meta-juego (Tienda, Inventario, Perfil) en Android Nativo con un núcleo jugable desarrollado en Unity 2D.

## 📱 Descripción del Proyecto

Este repositorio contiene el cliente Android del ecosistema DriveN'Dodge. La aplicación actúa como un "Launcher" y gestor de estado persistente, permitiendo al usuario autenticarse, gestionar sus recursos (monedas e ítems) y lanzar el motor de juego Unity pasando los datos necesarios en tiempo real.

### ✨ Características Principales

* **🤖 Asistente IA Integrado:** Chatbot inteligente incorporado ("Nuestra IA") que asiste al usuario durante la navegación y en la tienda.
* **🔐 Autenticación Segura:** Login y Registro de usuarios contra Backend REST.
* **🛒 Tienda Dinámica (Shop):** Sistema de compra de ítems (Boosters) consumiendo API REST.
* **🎒 Inventario Sincronizado:** Visualización en tiempo real de los objetos adquiridos.
* **🎮 Comunicación Bidireccional Android ↔ Unity:**
    * **Envío:** Inyección de inventario y datos de usuario al iniciar el juego.
    * **Recepción:** Captura de eventos del juego (partida finalizada) para actualizar BBDD.
* **💾 Persistencia de Datos:** Uso de `SharedPreferences` para caché local y sesiones.

---

## 🛠️ Stack Tecnológico

La arquitectura está basada en el patrón de capas para separar la lógica de negocio de la UI.

| Tecnología | Uso |
| :--- | :--- |
| **Java** | Lenguaje principal de desarrollo Android. |
| **Retrofit 2 + Gson** | Cliente HTTP para todas las peticiones a la API REST. |
| **Unity as a Library** | Integración del motor de juego como una `Activity` de Android. |
| **OkHttp3** | Interceptor y gestión de timeouts. |

---

## 📂 Estructura del Proyecto

El código está organizado en paquetes semánticos para facilitar la mantenibilidad:

```text
edu.upc.dsa_android_DriveNdodge
├── 📁 ui            # Lógica de pantallas (LoginActivity, PortalPageActivity, ShopActivity)
├── 📁 api           # Interfaces de Retrofit (ShopService, GameService)
├── 📁 models        # DTOs y POJOs (ItemInventario, User, InventarioRequest)
├── 📁 receivers     # BroadcastReceivers para comunicación con Unity

```
---

## 🔗 Enlaces del Proyecto
Este repositorio trabaja en conjunto con el servidor Backend:

🌐 Backend Repository: https://github.com/Pablcl/DSA-DriveNdodge_Backend

---

## 📖 Documentación

Puedes encontrar la documentación detallada sobre la arquitectura, el flujo de datos y los diagramas de integración en esta Wiki:

🔗 **[Documentación completa del repositorio](https://deepwiki.com/pol-p/dsa-driveNdodge-android)**

---

## 👥 Autores

Proyecto desarrollado por el equipo de **DSA - UPC**:

* **Pablo Casado**
* **Pablo Santamaría**
* **Arnau Munté**
* **Paula Tolosa**
* **Pol Puig**
