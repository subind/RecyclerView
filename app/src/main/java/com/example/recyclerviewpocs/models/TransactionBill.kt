package com.example.recyclerviewpocs.models

data class TransactionBill(
    val childTitle: String,
    val rimNum: String,
    val dateTime: String,
    val amount: String,
    var childCheckStatus: Boolean,
    val cId: Int
)
