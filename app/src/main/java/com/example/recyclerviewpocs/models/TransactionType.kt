package com.example.recyclerviewpocs.models

import com.example.recyclerviewpocs.models.TransactionBill

data class TransactionType(
    val headerTitle: String,
    val hId: Int,
    val childrenList: List<TransactionBill>,
    var headerCheckStatus: Boolean
)
