CinePlusSpaApp
Esta es una aplicación Android simula una plataforma de cine y entretenimiento, enfocada en la gestión de usuarios, autenticación y perfiles.
1.	Caso elegido
CinePlus combina streaming con compra de entradas físicas, integrando cine tradicional con experiencias digitales. La empresa busca modernizar la gestión de licencias y usuarios, ofreciendo transmisión segura, recomendaciones personalizadas y apoyo a la industria cultural nacional mediante la difusión de cine independiente. Alcance : Diseño/UI: Interfaz moderna con Jetpack Compose, temas adaptables y animaciones para transiciones. Validaciones: Formularios de registro y login con chequeos en tiempo real (email, password, etc.). Navegación : fluida gracias al uso de navbar. Estado: Gestión reactiva de estados de carga y error en operaciones. Persistencia: Persistencia de los datos, con el login AuthRepository.login(...) → Result (mapeado desde LoginResponse) Recursos nativos: Integración con localización por búsqueda de locales cercanos, notificaciones del dispositivo para alertar al usuario de usos y recordatorios. Consumo de API: Integración con backend para localización gps.
2.	Requisitos y ejecución
Stack:
UI: Jetpack Compose 
Navegación: Navigation Compose 
Librerias: libs.plugins.android.application
 libs.plugins.kotlin.android 
libs.plugins.kotlin.compose
Comandos e instalación:
Instalación: Clona el repositorio: git clone https://github.com/VicenteVergaraV/CinePlusSpaApp.git Abre el proyecto en Android Studio (versión 29 o posterior). Sincroniza Gradle: File > Sync Project with Gradle Files. Asegúrate de tener el SDK Android 34 instalado y configura un emulador o dispositivo físico. Ejecución: Comando: ./gradlew assembleDebug (para build) o usa el botón Run en Android Studio.
3.	Arquitectura y flujo
Estructura carpetas: (Resumen general)
app/src/main/java/com/cineplus/...:
app/src/main/java/com/cineplus/ui/screens app/src/main/java/com/cineplus/ui/profile app/src/main/java/com/cineplus/ui/theme app/src/main/java/com/cineplus/ui/utils app/src/main/java/com/cineplus/ui/viewmodel app/src/main/java/com/cineplus/ui/res
ui/components: Reutilizables como botones, cards de películas. ui/navigation: NavHost y rutas. ui/state: ViewModels y estados (e.g., AuthViewModel, ProfileState). data: Repositorios y fuentes de datos (local/remote). domain: Modelos y use cases (e.g., User, AuthUseCase). app/src/main/res: Recursos como drawables, strings, themes.
Gestión de estado:
Se revisa el local por pantalla con ViewModel y Flow/StateFlow para reactividad. Global para auth token via SingleLiveEvent o similar.
 Flujo de datos: UI -> ViewModel -> Repository -> API/Local
Navegación:
Stack principal para auth flow (login -> home). 
Navbar para secciones post-login (Home/Perfil).
4.	Funcionalidades
Formulario validado: Registro con campos email, password , nombre.
Navegación: Flujo: Login/Register -> Home -> Profile. Manejo del logout. 
Gestión de estado: Estado de carga no  tiene pantalla de carga por la fluides de la app.
 Persistencia local: En el caso de que el usuario ya este previamente registrado , este pasara automáticamente a la pantalla a principal (home)
5.	Endpoints 
@POST("user/login") suspend fun login(@Body request: LoginRequest): LoginResponse
// Obtener usuario Actual @GET("user/me") suspend fun getCurrentUser(): UserDto
// Obtener una Lista de Usuarios @GET("users") suspend fun getUsers(): UsersResponse
// Buscar Usuarios por Nombre @GET("users/search") suspend fun searchUsers(@Query("q") query: String): UsersResponse
// Obtener Usuario por ID @GET("users/{id}") suspend fun getUserById(@Path("id") id: Int): UserDto
6.	User flows
Sin conexión  : “Sin internet” + botón Reintentar. - Carga contenido simulado o caché local. 
Permiso ubicación denegado : “Activa la ubicación para mejores recomendaciones”. 

