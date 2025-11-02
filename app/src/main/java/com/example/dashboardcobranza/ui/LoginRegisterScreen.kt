package com.example.dashboardcobranza.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dashboardcobranza.data.model.Usuario
import com.example.dashboardcobranza.ui.viewmodel.AuthViewModel

@Composable
fun LoginRegisterScreen(
    onLoginSuccess: (Usuario) -> Unit,
    onRegisterSuccess: (Usuario) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var showLogin by remember { mutableStateOf(true) }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthViewModel.AuthState.Authenticated -> {
                if (state.isNewUser) onRegisterSuccess(state.usuario) else onLoginSuccess(state.usuario)
                viewModel.resetAuthState()
            }
            is AuthViewModel.AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showLogin) {
                LoginContent(
                    viewModel = viewModel,
                    isLoading = authState is AuthViewModel.AuthState.Loading,
                    onNavigateToRegister = { showLogin = false }
                )
            } else {
                RegisterContent(
                    viewModel = viewModel,
                    isLoading = authState is AuthViewModel.AuthState.Loading,
                    onNavigateToLogin = { showLogin = true }
                )
            }
        }
    }
}

@Composable
private fun LoginContent(
    viewModel: AuthViewModel,
    isLoading: Boolean,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(32.dp))

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
        PasswordTextField(value = password, onValueChange = { password = it }, label = "Contraseña", isVisible = isPasswordVisible, onVisibilityChange = { isPasswordVisible = !isPasswordVisible })
        Button(
            onClick = { viewModel.login(email, password) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp)) else Text("Entrar")
        }
        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes una cuenta? Regístrate")
        }
    }
}

@Composable
private fun RegisterContent(
    viewModel: AuthViewModel,
    isLoading: Boolean,
    onNavigateToLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var numeroEmpleado by remember { mutableStateOf("") }
    var puesto by remember { mutableStateOf("") }
    var empresa by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(32.dp))

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = numeroEmpleado, onValueChange = { numeroEmpleado = it }, label = { Text("Número de Empleado") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = puesto, onValueChange = { puesto = it }, label = { Text("Puesto") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = empresa, onValueChange = { empresa = it }, label = { Text("Empresa") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
        
        PasswordTextField(value = password, onValueChange = { password = it }, label = "Contraseña", isVisible = isPasswordVisible, onVisibilityChange = { isPasswordVisible = !isPasswordVisible })
        PasswordTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirmar Contraseña", isVisible = isConfirmPasswordVisible, onVisibilityChange = { isConfirmPasswordVisible = !isConfirmPasswordVisible })
        
        Button(
            onClick = { 
                if (password == confirmPassword) {
                    // SOLUCIÓN: Se elimina la palabra "le" que causaba el error de sintaxis.
                    viewModel.register(nombre, email, password, numeroEmpleado, puesto, empresa, ciudad)
                } else {
                    // Aquí se podría mostrar un Toast o un error en los campos de texto.
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp)) else Text("Registrarse")
        }
        
        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes una cuenta? Inicia Sesión")
        }
    }
}
