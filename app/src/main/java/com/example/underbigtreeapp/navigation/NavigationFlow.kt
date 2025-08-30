package com.example.underbigtreeapp.navigation

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.underbigtreeapp.data.local.AppDatabase
import com.example.underbigtreeapp.repository.MenuRepository
import com.example.underbigtreeapp.ui.customerHomePage.CustHomeScreen
import com.example.underbigtreeapp.ui.order.OrderScreen
import com.example.underbigtreeapp.ui.payment.BankPaymentScreen
import com.example.underbigtreeapp.ui.payment.BankPaymentSuccess
import com.example.underbigtreeapp.ui.payment.TngPaymentScreen
import com.example.underbigtreeapp.ui.payment.TngPaymentSuccess
import com.example.underbigtreeapp.viewModel.CustHomeViewModel
import com.example.underbigtreeapp.viewModel.CustHomeViewModelFactory
import kotlin.getValue

@Composable
fun NavigationFlow(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = "order"
    ) {
        composable("order") {
            OrderScreen(
                onBackClick = { navController.popBackStack() },
                onPlaceOrder = { cartItem ->
                    navController.navigate("tngPayment")
                }
            )
        }

        composable("tngPayment") {
            TngPaymentScreen(
                onPayClick = { formattedAmount ->
                    navController.navigate("tngSuccess")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("tngSuccess") { backStackEntry ->
            TngPaymentSuccess(
                onReturnClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("bankPayment") {
            BankPaymentScreen(
                onReject = {},
                onApprove = { formattedAmount->
                    navController.navigate("bankSuccess")
                },
            )
        }

        composable("bankSuccess") { backStackEntry ->
            BankPaymentSuccess(
                onDoneClick={}
            )
        }

    }
//    NavHost(navController = navController, startDestination = "home"){
//        composable("home") {
//            val context = LocalContext.current
//            val database = AppDatabase.getDatabase(context)
//            val repository = remember { MenuRepository(database) }
//
//            val viewModel: CustHomeViewModel = viewModel(factory = CustHomeViewModelFactory(repository))
//            CustHomeScreen(points = 0, modifier = Modifier, viewModel = viewModel)
//        }
//    }
}