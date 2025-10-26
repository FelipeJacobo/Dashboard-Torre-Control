package com.example.dashboardcobranza.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dashboardcobranza.data.model.Usuario
// El nombre de tu ViewModel es LoginViewModel, no LoginRegisterViewModel. Lo corregimos.
import com.example.dashboardcobranza.ui.viewmodel.LoginViewModel

// Un pequeño interruptor para saber si estamos en modo Login o Registro.
private enum class AuthMode {
    LOGIN,
    REGISTER
}

/**
 * La puerta de entrada a la app. Aquí manejamos tanto el inicio de sesión como el registro de nuevos usuarios.
 */
@Composable
fun LoginRegisterScreen(
    onLoginSuccess: (Usuario) -> Unit,
    onRegisterSuccess: (Usuario) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Estados para controlar los campos de texto y el modo actual (login/registro).
    var authMode by remember { mutableStateOf(AuthMode.LOGIN) }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Nos suscribimos a los cambios del ViewModel para reaccionar a lo que pasa.
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current // Para mostrar Toasts y mensajes.

    // Este es nuestro "espía". Se activa cada vez que el `uiState` del ViewModel cambia.
    LaunchedEffect(key1 = uiState) {
        when {
            // ¡Éxito en Login!
            uiState.loginSuccess && uiState.usuarioLogueado != null -> {
                Toast.makeText(context, "¡Bienvenido de vuelta!", Toast.LENGTH_SHORT).show()
                val usuario = uiState.usuarioLogueado!!
                viewModel.resetState()
                onLoginSuccess(usuario) // Avisamos a AppNavigation para que inicie sesión y navegue.
            }

            // ¡Nuevo usuario en la casa!
            uiState.registrationSuccess && uiState.usuarioLogueado != null -> {
                Toast.makeText(context, "¡Registro completado!", Toast.LENGTH_SHORT).show()
                val usuario = uiState.usuarioLogueado!!
                viewModel.resetState()
                // En lugar de volver al login, llamamos a onRegisterSuccess para iniciar sesión automáticamente.
                onRegisterSuccess(usuario)
            }

            // Algo salió mal, mostramos el error que nos manda el ViewModel.
            uiState.error != null -> {
                Toast.makeText(context, uiState.error, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mientras el ViewModel trabaja, mostramos una ruedita para que el usuario no se desespere.
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            // Si no estamos cargando, mostramos el formulario.
            AuthForm(
                authMode = authMode,
                nombre = nombre,
                onNombreChange = { nombre = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                onAuthAction = {
                    // ¿Qué botón apretó el usuario?
                    if (authMode == AuthMode.LOGIN) {
                        viewModel.login(email, password) // A intentar el login.
                    } else {
                        // Validaciones básicas antes de intentar registrar.
                        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Oye, rellena todos los campos.", Toast.LENGTH_SHORT).show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(context, "Las contraseñas no son gemelas, ¡revísalas!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.register(nombre, email, password) // Todo en orden, a registrar.
                        }
                    }
                },
                onSwitchMode = {
                    // El clásico "cambio de bando".
                    authMode = if (authMode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
                }
            )
        }
    }
}

/**
 * El formulario reutilizable con todos los campos de texto y botones.
 * No tiene ni idea de la lógica, solo pinta lo que le dicen y avisa cuando algo cambia.
 */
@Composable
private fun AuthForm(
    authMode: AuthMode,
    nombre: String, onNombreChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    confirmPassword: String, onConfirmPasswordChange: (String) -> Unit,
    onAuthAction: () -> Unit,
    onSwitchMode: () -> Unit
) {
    // --- INICIO DE LA MEJORA ---
    // 1. Estados para controlar la visibilidad de cada campo de contraseña
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    // --- FIN DE LA MEJORA ---

    Text(
        text = if (authMode == AuthMode.LOGIN) "Iniciar Sesión" else "Crear Cuenta",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(24.dp))

    // El campo de nombre solo se muestra si estamos en modo registro.
    if (authMode == AuthMode.REGISTER) {
        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre Completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Correo Electrónico") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))

    // --- INICIO DE LA MEJORA: CAMPO DE CONTRASEÑA ---
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Contraseña") },
        singleLine = true,
        // 2. Si passwordVisible es true, no aplicamos transformación (se ve el texto).
        // Si es false, usamos PasswordVisualTransformation (se ven puntitos).
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        // 3. Añadimos el icono al final del campo
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

            // IconButton hace que el icono sea clickeable
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
    // --- FIN DE LA MEJORA ---
    Spacer(modifier = Modifier.height(16.dp))

    // El campo de confirmar contraseña solo se muestra en modo registro.
    if (authMode == AuthMode.REGISTER) {
        // --- INICIO DE LA MEJORA: CAMPO DE CONFIRMAR CONTRASEÑA ---
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirmar Contraseña") },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image = if (confirmPasswordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )
        // --- FIN DE LA MEJORA ---
        Spacer(modifier = Modifier.height(24.dp))
    }

    Button(
        onClick = onAuthAction,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(text = if (authMode == AuthMode.LOGIN) "Entrar" else "Registrarse")
    }

    Spacer(modifier = Modifier.height(24.dp))

    TextButton(onClick = onSwitchMode) {
        Text(
            textAlign = TextAlign.Center,
            text = if (authMode == AuthMode.LOGIN) {
                "¿No tienes una cuenta? Regístrate"
            } else {
                "¿Ya tienes una cuenta? Inicia Sesión"
            }
        )
    }
}
