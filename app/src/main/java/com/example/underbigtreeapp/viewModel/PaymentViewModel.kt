package com.example.underbigtreeapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.underbigtreeapp.utils.formatAmount
import com.example.underbigtreeapp.utils.maskPhoneNumber

class PaymentViewModel : ViewModel() {
    private val phoneNumber = "+60123456780" //from profile
    private val transactionDate = "28 Aug 2025"

    fun getMaskedPhone(): String = maskPhoneNumber(phoneNumber)
    fun getTransactionDate(): String = transactionDate
}