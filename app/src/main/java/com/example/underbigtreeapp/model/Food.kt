package com.example.underbigtreeapp.model

data class Food(
    val name: String,
    val price: Double,
    val description: String,
    val sauces: List<Option>,
    val addOns: List<Option>
)
