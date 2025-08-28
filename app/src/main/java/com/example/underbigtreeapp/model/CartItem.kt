package com.example.underbigtreeapp.model

data class CartItem(
    val food: Food,
    val selectedSauces: List<Option>,
    val selectedAddOns: List<Option>,
    val quantity: Int,
    val takeAway: Option?,
    val remarks: String,
    val totalPrice: Double
)
