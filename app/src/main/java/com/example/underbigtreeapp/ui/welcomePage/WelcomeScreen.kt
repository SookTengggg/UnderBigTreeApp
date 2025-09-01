package com.example.underbigtreeapp.ui.welcomePage

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.underbigtreeapp.viewModel.UserViewModel

@Composable
fun WelcomeScreen(
    userViewModel: UserViewModel,
    onGoToProfile: () -> Unit,
    onGoToPoints: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    val user by userViewModel.user.observeAsState()
    val loading by userViewModel.loading.observeAsState(false)
    val error by userViewModel.error.observeAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(24.dp))

            when {
                loading -> {
                    CircularProgressIndicator()
                }
                user != null -> {
                    Text("Hello, ${user!!.displayName}!", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onGoToProfile, modifier = Modifier.fillMaxWidth()) {
                        Text("Profile")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = onGoToPoints, modifier = Modifier.fillMaxWidth()) {
                        Text("Points")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                        Text("Logout")
                    }
                }
                else -> {
                    error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Login") }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onNavigateToSignup,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Sign Up") }
                }
            }
        }
    }
}
