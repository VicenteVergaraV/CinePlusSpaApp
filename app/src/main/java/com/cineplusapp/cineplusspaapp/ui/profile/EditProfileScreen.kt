package com.cineplusapp.cineplusspaapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.EditProfileViewModel

@Composable
fun EditProfileScreen(vm: EditProfileViewModel = hiltViewModel(), onSaved: () -> Unit) {
    val user by vm.user.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            nombre = it.nombre ?: ""
            telefono = it.telefono ?: ""
            avatar = it.avatar ?: ""
        }
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Editar Perfil", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
        OutlinedTextField(telefono, { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        OutlinedTextField(avatar, { avatar = it }, label = { Text("Avatar URL") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

        Button(
            onClick = { vm.updateUserProfile(nombre, telefono, avatar, onSaved) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Guardar")
        }
    }
}
