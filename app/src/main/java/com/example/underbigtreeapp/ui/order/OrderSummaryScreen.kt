package com.example.underbigtreeapp.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.underbigtreeapp.viewModel.OrderViewModel

@Composable
fun OrderSummaryScreen(
    viewModel: OrderViewModel,
    onCheckout: (Double, String) -> Unit,
    onBackClick: () -> Unit = {}
){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal=25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text="Order Summary",
            style = MaterialTheme.typography.titleLarge,
            fontSize=32.sp
        )
        Spacer(modifier = Modifier.height(10.dp))



        Spacer(Modifier.height(16.dp))

        Text("Subtotal: RM ")
        Spacer(Modifier.height(16.dp))

        Text("Payment Option *")
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            RadioButton(
//                selected = selectedPayment == "tng",
//                onClick = { viewModel.setPaymentMethod("tng") }
//            )
//            Text("Touch n Go")
//        }
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            RadioButton(
//                selected = selectedPayment == "bank",
//                onClick = { viewModel.setPaymentMethod("bank") }
//            )
//            Text("Credit/Debit Card")
//        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {  }
        ) {
            Text("Pay")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderSummaryScreen() {

}