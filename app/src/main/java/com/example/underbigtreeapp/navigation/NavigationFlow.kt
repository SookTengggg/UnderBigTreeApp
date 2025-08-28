package com.example.underbigtreeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.underbigtreeapp.ui.order.OrderScreen
import com.example.underbigtreeapp.ui.payment.BankPaymentScreen
import com.example.underbigtreeapp.ui.payment.BankPaymentSuccess
import com.example.underbigtreeapp.ui.payment.TngPaymentScreen
import com.example.underbigtreeapp.ui.payment.TngPaymentSuccess

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
}