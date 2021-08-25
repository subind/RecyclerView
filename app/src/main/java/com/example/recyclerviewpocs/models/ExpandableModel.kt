package com.example.recyclerviewpocs.models

class ExpandableModel {

    companion object{
        const val HEADER = 1
        const val CHILD = 2
    }

    var type: Int
    var isExpanded: Boolean
    lateinit var header: TransactionType
    lateinit var child: TransactionBill

    constructor(type: Int, header: TransactionType, isExpanded: Boolean = false) {
        this.type = type
        this.isExpanded = isExpanded
        this.header = header
    }

    constructor(type: Int, child: TransactionBill, isExpanded: Boolean = false) {
        this.type = type
        this.isExpanded = isExpanded
        this.child = child
    }

}