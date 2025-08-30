package com.example.underbigtreeapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.underbigtreeapp.model.CartItem
import com.example.underbigtreeapp.model.Food
import com.example.underbigtreeapp.model.Option
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addCartItem(item: CartItem) {
        _cartItems.value = _cartItems.value + item
    }

    fun removeCartItem(index: Int) {
        if (index in _cartItems.value.indices) {
            _cartItems.value = _cartItems.value.toMutableList().apply { removeAt(index) }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun calculateTotal(): Double {
        return _cartItems.value.sumOf { it.totalPrice }
    }

    fun loadDummyCartItems() {
        val demoFood = Food(
            name = "Spaghetti(Fish)",
            price = 10.50,
            description = "A type of noodles.",
            sauces = listOf(
                Option("Tomato", 0.0),
                Option("Cheese", 0.0)
            ),
            addOns = listOf(
                Option("Egg", 1.0),
                Option("Hot dog", 1.0)
            )
        )

        val demoCartItem = CartItem(
            food = demoFood,
            selectedSauces = listOf(Option("Tomato", 0.0)),
            selectedAddOns = listOf(Option("Egg", 1.0)),
            quantity = 2,
            takeAway = Option("Yes", 0.50),
            remarks = "Extra cheese",
            totalPrice = (demoFood.price + 1.0 + 0.50) * 2
        )

        _cartItems.value = listOf(demoCartItem)
    }
}
