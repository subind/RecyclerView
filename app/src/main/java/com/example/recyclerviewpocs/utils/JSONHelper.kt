package com.example.recyclerviewpocs.utils

import com.example.recyclerviewpocs.MyApplication
import com.example.recyclerviewpocs.models.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object JSONHelper {

    fun fetchParsedJSONInfo() : Transaction {
        val jsonfile: String = MyApplication.instance.applicationContext.assets.open("transaction.json").bufferedReader().use {it.readText()}
        println("Parsed JSON file is.... $jsonfile")

        val gson = Gson()
        val stateCapitalModelType: Type = object : TypeToken<Transaction>() {}.type
        var headerModel = gson.fromJson<Transaction>(jsonfile,stateCapitalModelType)
        println("Output in String ${headerModel.toString()}")
        return headerModel

    }

}