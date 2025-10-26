package com.example.dashboardcobranza.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf("dashboard" to Icons.Filled.Home, "alerts" to Icons.Filled.Notifications, "profile" to Icons.Filled.Person)
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    NavigationBar {
        items.forEach { pair ->
            val route = pair.first
            val icon = pair.second
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = route) },
                label = { Text(route) },
                selected = currentRoute == route,
                onClick = { navController.navigate(route) }
            )
        }
    }
}
