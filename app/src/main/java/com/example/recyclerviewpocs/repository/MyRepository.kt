package com.example.recyclerviewpocs.repository

import com.example.recyclerviewpocs.models.Transaction
import com.example.recyclerviewpocs.utils.JSONHelper

class MyRepository {
    fun fetchData() : Transaction = JSONHelper.fetchParsedJSONInfo()
}