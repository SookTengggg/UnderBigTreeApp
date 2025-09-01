package com.example.underbigtreeapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.underbigtreeapp.model.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderSummaryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _orders = MutableStateFlow<List<CartItem>>(emptyList())
    val orders: StateFlow<List<CartItem>> = _orders

    fun fetchOrders() {
        db.collection("Orders")
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { snap ->
                    snap.toObject(CartItem::class.java).copy(orderId = snap.id)
                }
                _orders.value = list
            }
    }

    fun deleteOrder(orderId: String) {
        db.collection("Orders").document(orderId)
            .delete()
            .addOnSuccessListener {
                _orders.value = _orders.value.filterNot { it.orderId == orderId }
            }
    }


}

