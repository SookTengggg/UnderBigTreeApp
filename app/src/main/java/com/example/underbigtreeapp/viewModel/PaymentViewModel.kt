package com.example.underbigtreeapp.viewModel


import androidx.lifecycle.ViewModel
import com.example.underbigtreeapp.utils.maskPhoneNumber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PaymentViewModel : ViewModel() {
    private val phoneNumber = "+60123456780" //from profile

    fun getMaskedPhone(): String = maskPhoneNumber(phoneNumber)

    fun getTransactionDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
        return current.format(formatter)
    }
}