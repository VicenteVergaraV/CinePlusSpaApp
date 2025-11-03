CinePlusSpaApp


Esta es una aplicación Android desarrollada como parte de un proyecto académico para la Entrega Parcial 3 (EP3). La app simula una plataforma de cine y entretenimiento (inspirada en servicios como Netflix o similares), enfocada en la gestión de usuarios, autenticación y perfiles personalizados. Utiliza tecnologías modernas de Android para proporcionar una experiencia fluida y nativa.
1. Caso elegido y alcance

Caso: Plataforma de streaming de cine y series (CinePlus), donde los usuarios pueden registrarse, iniciar sesión, gestionar su perfil (incluyendo avatar) y acceder a contenido. El enfoque está en la autenticación segura, persistencia local de datos de usuario y navegación intuitiva entre pantallas principales.
Alcance EP3:

Diseño/UI: Interfaz moderna con Jetpack Compose, temas adaptables y animaciones suaves para transiciones.
Validaciones: Formularios de registro y login con chequeos en tiempo real (email, password, etc.).
Navegación: Flujo principal con backstack y tabs para secciones como Home, Perfil y Configuración.
Estado: Gestión reactiva de estados de carga, éxito y error en operaciones críticas.
Persistencia: Almacenamiento local de datos de usuario y preferencias.
Recursos nativos: Integración con cámara y galería para subir avatar de perfil, con manejo de permisos.
Animaciones: Transiciones animadas en navegación y carga de contenido para mejorar UX.
Consumo de API: Integración con backend para auth y perfil usuario.



2. Requisitos y ejecución

Stack:

Lenguaje: Kotlin
UI: Jetpack Compose
Navegación: Navigation Compose
Persistencia: Room (para datos locales) + SharedPreferences
Red: Retrofit con OkHttp para llamadas API
Inyección de dependencias: Hilt
Otros: Coroutines/Flow para manejo asíncrono, Coil para carga de imágenes, Accompanist para permisos.


Instalación:

Clona el repositorio: git clone https://github.com/VicenteVergaraV/CinePlusSpaApp.git
Abre el proyecto en Android Studio (versión Flamingo o superior recomendada).
Sincroniza Gradle: File > Sync Project with Gradle Files.
Asegúrate de tener el SDK Android 34 instalado y configura un emulador o dispositivo físico.


Ejecución:

Comando: ./gradlew assembleDebug (para build) o usa el botón Run en Android Studio.
Perfiles:

Debug: Para desarrollo con logs habilitados.
Release: Para pruebas, genera APK en app/build/outputs/apk/release/.





3. Arquitectura y flujo

Estructura carpetas: (Resumen general)

app/src/main/java/com/cineplus/...:

ui/screens: Pantallas Compose (e.g., LoginScreen, RegisterScreen, ProfileScreen).
ui/components: Reutilizables como botones, cards de películas.
ui/navigation: NavHost y rutas.
ui/state: ViewModels y estados (e.g., AuthViewModel, ProfileState).
data: Repositorios y fuentes de datos (local/remote).
domain: Modelos y use cases (e.g., User, AuthUseCase).


app/src/main/res: Recursos como drawables, strings, themes.


Gestión de estado:

Estrategia: Local por pantalla con ViewModel y Flow/StateFlow para reactividad. Global para auth token via SingleLiveEvent o similar.
Flujo de datos: UI -> ViewModel (eventos) -> Repository (data layer) -> API/Local DB -> Estado observable -> UI.


Navegación:

Stack principal para auth flow (login -> home).
Tabs para secciones post-login (Home/Perfil).
Soporte para deep links en perfil (e.g., cineplus://profile).



4. Funcionalidades

Formulario validado: Registro con campos email, password (mín. 8 chars), nombre opcional; validaciones en Compose con regex y feedback visual.
Navegación y backstack: Flujo: Splash -> Login/Register -> Home (con tabs) -> Profile. Manejo de back para logout.
Gestión de estado: Estados de carga (progress bar), éxito (snackbar), error (retry button) en auth y perfil.
Persistencia local: CRUD en Room para usuarios locales; almacenamiento de imagen de perfil en caché con Coil.
Almacenamiento de imagen de perfil: Subida via API, fallback a drawable local si offline.
Recursos nativos: Acceso a cámara/galería via ActivityResultLauncher; permisos runtime con rationale y fallback a galería si cámara denegada.
Animaciones: Fade-in en carga de pantallas, slide para tabs; propósito: reducir percepción de latencia.
Consumo de API: Llamadas a /signup, /login, /me con token en headers; manejo de errores 4xx/5xx.

5. Endpoints
Base URL: https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW





























MétodoRutaBodyRespuestaPOST/auth/signup{ email, password, name?, ... }201 { authToken, user: { id, email, ... } }POST/auth/login{ email, password }200 { authToken, user: { id, email, ... } }GET/auth/me- (requiere header Authorization: Bearer)200 { id, email, name, avatarUrl?, ... }
6. User flows
Flujo principal: Registro y Acceso a Perfil

Inicio: Splash screen (2s) -> Si no auth, a LoginScreen.
Registro/Login: Usuario ingresa datos -> Validación -> Llamada API -> Si éxito, guarda token local y navega a Home.
Home: Tabs con contenido simulado (películas) -> Usuario va a Profile.
Perfil: Carga /me -> Muestra datos, opción subir avatar (cámara/galería) -> Actualiza via API.
Logout: Limpia token y back a Login.

Casos de error:

Error de red en auth: Muestra "Sin conexión, reintenta" con botón retry; fallback a modo offline si token existe.
Credenciales inválidas: Snackbar "Email o password incorrecto" sin navegar.
Permisos denegados para cámara: Diálogo explicativo -> Fallback a galería -> Si todo denegado, usa avatar default.
Error en /me: Carga datos locales cached; si no, logout forzado.

Para diagramas visuales, recomiendo herramientas como Lucidchart o Draw.io. Ejemplo textual (Mermaid-like):
textgraph TD
    A[Login/Register] -->|Éxito| B[Home Tabs]
    B --> C[Profile]
    C -->|Subir Avatar| D[Cámara/Galería]
    D -->|OK| C
    A -->|Error| E[Retry Snackbar]
    C -->|Error /me| F[Logout -> Login]
¡Gracias por revisar el proyecto! Si tienes sugerencias, abre un issue.
