package com.example.recyclerviewpocs.models

import com.example.recyclerviewpocs.models.TransactionBill

data class TransactionType(val headerTitle: String, val childrenList: List<TransactionBill>)
