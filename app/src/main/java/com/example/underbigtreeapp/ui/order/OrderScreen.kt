package com.example.underbigtreeapp.ui.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.underbigtreeapp.R
import com.example.underbigtreeapp.model.CartItem
import com.example.underbigtreeapp.model.Option
import com.example.underbigtreeapp.viewModel.OrderViewModel

@Composable
fun OrderScreen(
    viewModel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit = {},
    onPlaceOrder: (CartItem) -> Unit={}
) {
    val food by viewModel.food.collectAsState()
    val selectedSauces by viewModel.selectedSauces.collectAsState()
    val selectedAddOns by viewModel.selectedAddOns.collectAsState()
    val takeAway by viewModel.takeAway.collectAsState()
    val remarks by viewModel.remarks.collectAsState()
    val quantity by viewModel.quantity.collectAsState()

    var showSauceWarning by remember { mutableStateOf(false) }

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
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Food",
            modifier = Modifier.size(90.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    food.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text("RM ${food.price}")
            }

            Text(food.description, color = Color.Gray)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sauce")
                Text("*Select one or more", color = Color.Red)
            }
            food.sauces.forEach { sauce ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedSauces.contains(sauce),
                            onCheckedChange = { viewModel.toggleSauce(sauce, it) }
                        )
                        Text(sauce.name)
                    }
                    Text("+ RM ${sauce.price}")
                }

            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Add On")
                Text("*Optional", color = Color.Red)
            }
            food.addOns.forEach { addOn ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedAddOns.contains(addOn),
                            onCheckedChange = { viewModel.toggleAddOn(addOn, it) }
                        )
                        Text(addOn.name)
                    }
                    Text("+ RM ${addOn.price}")
                }

            }

            Spacer(Modifier.height(16.dp))
            Text("Take Away")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = takeAway?.name == "Yes",
                        onClick = { viewModel.setTakeAway(Option("Yes", 0.50)) }
                    )
                    Text("Yes")
                }
                Text("+ RM 0.50")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = takeAway?.name == "No",
                        onClick = { viewModel.setTakeAway(Option("No", 0.0)) }
                    )
                    Text("No")
                }
                Text("+ RM 0.00")
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Remarks")
                Text("*Optional", color = Color.Red)
            }
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = remarks,
                onValueChange = { viewModel.setRemarks(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.decreaseQuantity() }) { Text("-") }
            Text(quantity.toString())
            IconButton(onClick = { viewModel.increaseQuantity() }) { Text("+") }
        }
        Spacer(Modifier.height(4.dp))

        Button(
            onClick = {
                if (selectedSauces.isEmpty()) {
                    showSauceWarning = true
                } else {
                    onPlaceOrder(viewModel.buildCartItem())
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Cart")
        }

        if (showSauceWarning) {
            AlertDialog(
                onDismissRequest = { showSauceWarning = false },
                title = { Text("Selection Required") },
                text = { Text("Please select at least one sauce before placing your order.") },
                confirmButton = {
                    TextButton(onClick = { showSauceWarning = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderScreen() {
    OrderScreen()
}
