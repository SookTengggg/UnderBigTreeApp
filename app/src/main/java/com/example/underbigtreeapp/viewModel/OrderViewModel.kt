package com.example.underbigtreeapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.underbigtreeapp.model.CartItem
import com.example.underbigtreeapp.model.Food
import com.example.underbigtreeapp.model.Option
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel : ViewModel() {

    private val _food = MutableStateFlow(
        Food(
            name = "Spaghetti(Fish)",
            price = 10.50,
            description = "A type of noodles.",
            sauces = listOf(
                Option("Tomato", 0.0),
                Option("Cheese", 0.0),
                Option("Mushroom", 0.0)
            ),
            addOns = listOf(
                Option("Egg", 1.0),
                Option("Hot dog", 1.0),
                Option("Hash Brown", 1.5)
            )
        )
    )
    val food: StateFlow<Food> = _food

    private val _selectedSauces = MutableStateFlow(setOf<Option>())
    val selectedSauces: StateFlow<Set<Option>> = _selectedSauces

    private val _selectedAddOns = MutableStateFlow(setOf<Option>())
    val selectedAddOns: StateFlow<Set<Option>> = _selectedAddOns

    private val _takeAway = MutableStateFlow<Option?>(Option("No", 0.0))
    val takeAway: StateFlow<Option?> = _takeAway

    private val _remarks = MutableStateFlow("")
    val remarks: StateFlow<String> = _remarks

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity

    fun toggleSauce(sauce: Option, checked: Boolean) {
        _selectedSauces.value = if (checked) _selectedSauces.value + sauce else _selectedSauces.value - sauce
    }

    fun toggleAddOn(addOn: Option, checked: Boolean) {
        _selectedAddOns.value = if (checked) _selectedAddOns.value + addOn else _selectedAddOns.value - addOn
    }

    fun setTakeAway(option: Option) {
        _takeAway.value = option
    }

    fun setRemarks(value: String) {
        _remarks.value = value
    }

    fun increaseQuantity() {
        _quantity.value++
    }

    fun decreaseQuantity() {
        if (_quantity.value > 1) _quantity.value--
    }

    fun buildCartItem(): CartItem {
        val base = _food.value.price
        val addOnsPrice = _selectedAddOns.value.sumOf { it.price }
        val saucesPrice = _selectedSauces.value.sumOf { it.price }
        val takeAwayPrice = _takeAway.value?.price ?: 0.0

        val total = (base + addOnsPrice + saucesPrice + takeAwayPrice) * _quantity.value

        return CartItem(
            food = _food.value,
            selectedSauces = _selectedSauces.value.toList(),
            selectedAddOns = _selectedAddOns.value.toList(),
            quantity = _quantity.value,
            takeAway = _takeAway.value,
            remarks = _remarks.value,
            totalPrice = total
        )
    }

}