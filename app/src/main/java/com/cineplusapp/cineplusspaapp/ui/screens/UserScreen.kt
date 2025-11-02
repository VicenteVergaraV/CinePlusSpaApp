package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

data class SimpleUser(val id: Int, val name: String, val email: String, val avatar: String?)

@Composable
fun UsersScreen(onClickUser: (Int) -> Unit) {
    // TODO: reemplazar por ViewModel + ApiService.getUsers() / searchUsers()
    val users = remember {
        listOf(
            SimpleUser(1, "Ana Pérez", "ana@example.com", "https://i.pravatar.cc/128?img=1"),
            SimpleUser(2, "Luis Soto", "luis@example.com", "https://i.pravatar.cc/128?img=2"),
            SimpleUser(3, "Camila Rojas", "camila@example.com", "https://i.pravatar.cc/128?img=3")
        )
    }

    LazyColumn(Modifier.fillMaxSize().padding(12.dp)) {
        items(users) { u ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    .clickable { onClickUser(u.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (u.avatar != null) {
                        Image(
                            painter = rememberAsyncImagePainter(u.avatar),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Column(Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(u.name, style = MaterialTheme.typography.titleMedium)
                        Text(u.email, style = MaterialTheme.typography.bodyMedium)
                    }
                    Text("#${u.id}")
                }
            }
        }
    }
}
