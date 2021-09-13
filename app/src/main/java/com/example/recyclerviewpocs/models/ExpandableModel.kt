package com.example.recyclerviewpocs.models

class ExpandableModel {

    companion object{
        const val HEADER = 1
        const val CHILD = 2
    }

    var type: Int = 0
    var isExpanded: Boolean = false
    var header: TransactionType? = null
    var child: TransactionBill? = null
    var drawable: Int = 0

}