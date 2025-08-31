package com.example.underbigtreeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.underbigtreeapp.data.local.AppDatabase
import com.example.underbigtreeapp.repository.MenuRepository
import com.example.underbigtreeapp.ui.customerHomePage.CustHomeScreen
import com.example.underbigtreeapp.ui.loginPage.LoginScreen
import com.example.underbigtreeapp.ui.order.OrderScreen
import com.example.underbigtreeapp.ui.order.OrderSummaryScreen
import com.example.underbigtreeapp.ui.payment.BankPaymentScreen
import com.example.underbigtreeapp.ui.payment.BankPaymentSuccess
import com.example.underbigtreeapp.ui.payment.TngPaymentScreen
import com.example.underbigtreeapp.ui.payment.TngPaymentSuccess
import com.example.underbigtreeapp.ui.pointPage.PointScreen
import com.example.underbigtreeapp.ui.profilePage.ProfileScreen
import com.example.underbigtreeapp.ui.signupPage.SignupScreen
import com.example.underbigtreeapp.ui.welcomePage.WelcomeScreen
import com.example.underbigtreeapp.viewModel.CartViewModel
import com.example.underbigtreeapp.viewModel.CustHomeViewModel
import com.example.underbigtreeapp.viewModel.CustHomeViewModelFactory
import com.example.underbigtreeapp.viewModel.OrderSummaryViewModel
import com.example.underbigtreeapp.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NavigationFlow(navController: NavHostController) {
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination = if (isLoggedIn) "home" else "welcome"

    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("welcome") {
            WelcomeScreen(
                userViewModel = userViewModel,
                onGoToProfile = { navController.navigate("profile") },
                onGoToPoints = { navController.navigate("points") },
                onLogout = {
                    userViewModel.logout()
                    navController.navigate("welcome") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToSignup = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate("welcome") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onGoToLogin = { navController.navigate("login") },
                userViewModel = userViewModel
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToSignup = { navController.navigate("signup") },
                userViewModel = userViewModel
            )
        }

        composable("profile") {
            ProfileScreen(
                userViewModel = userViewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    userViewModel.logout()
                    navController.navigate("welcome") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("points") {
            PointScreen(
                userViewModel = userViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("home") {
            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val repository = remember { MenuRepository(database) }
            val cartViewModel: CartViewModel = viewModel()
            val viewModel: CustHomeViewModel = viewModel(factory = CustHomeViewModelFactory(repository))

            CustHomeScreen(
                viewModel = viewModel,
                navController = navController,
                cartViewModel = cartViewModel,
                userViewModel = userViewModel,
                onGoToPoints = { navController.navigate("points") },
                onGoToProfile = { navController.navigate("profile") }
            )
        }

        composable("order/{foodId}") {backStackEntry ->
            val foodId = backStackEntry.arguments?.getString("foodId") ?: ""
            OrderScreen(
                foodId = foodId,
                onBackClick = { navController.popBackStack() },
                onPlaceOrder = { cartItem ->
                    navController.navigate("orderSummaryScreen")
                }
            )
        }

        composable("orderSummaryScreen") {
            val summaryViewModel: OrderSummaryViewModel = viewModel()
            OrderSummaryScreen(
                viewModel = summaryViewModel,
                navController,
                onBackClick = { navController.popBackStack() },
            )
        }

        composable("tngPayment/{totalAmount}") { backStackEntry ->
            val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
            TngPaymentScreen(
                totalAmount = totalAmount,
                onPayClick = { formattedAmount ->
                    navController.navigate("tngSuccess/$totalAmount")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("tngSuccess/{totalAmount}") { backStackEntry ->
            val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
            TngPaymentSuccess(
                totalAmount = totalAmount,
                onReturnClick = {
                    navController.navigate("home")
                }
            )
        }

        composable("bankPayment/{totalAmount}") {backStackEntry ->
            val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
            BankPaymentScreen(
                totalAmount = totalAmount,
                onReject = {navController.popBackStack()},
                onApprove = { formattedAmount->
                    navController.navigate("bankSuccess/$totalAmount")
                },
            )
        }

        composable("bankSuccess/{totalAmount}") { backStackEntry ->
            val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
            BankPaymentSuccess(
                totalAmount = totalAmount,
                onDoneClick ={navController.navigate("home")}
            )
        }
    }
}