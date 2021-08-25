package com.example.recyclerviewpocs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recyclerviewpocs.repository.MyRepository
import kotlinx.coroutines.Dispatchers

class MainViewModelFactory : ViewModelProvider.Factory {

    lateinit var myRepository: MyRepository
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        myRepository = MyRepository()
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(Dispatchers.IO, myRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}