package com.example.recyclerviewpocs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewpocs.adapter.MyExpandableAdapter
import com.example.recyclerviewpocs.models.ExpandableModel
import com.example.recyclerviewpocs.utils.ResultOf
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var myViewModel: MainViewModel
    var myExpandableAdapter : MyExpandableAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        myViewModel.obtainCountryStateCapitals()
        observeViewModelResponse()
    }

    private fun initViewModel(){
        var mainViewModelFactory = MainViewModelFactory()
        myViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
    }

    private fun observeViewModelResponse(){

        myViewModel.obtainCountryStatesResponse.observe(this, Observer {
            it?.let {
                when(it){

                    is ResultOf.Success -> {
                        val countryStateInfo =  myViewModel.prepareDataForExpandableAdapter(it.value)
                        populateAdapterWithInfo(countryStateInfo)
                        setNumberOfPendingItemsText(countryStateInfo)
                    }

                    is ResultOf.Failure -> {
                        val failedMessage =  it.message ?: "Unknown Error"
                        println("Failed Message $failedMessage")
                    }

                }
            }
        })
    }

    private fun populateAdapterWithInfo(expandableCountryStateList : MutableList<ExpandableModel>){
        myExpandableAdapter = MyExpandableAdapter(expandableCountryStateList)
        myExpandableAdapter?.let {
            val layoutManager = LinearLayoutManager(this)
            rv.layoutManager = layoutManager
            rv.adapter = it
            //rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            it.notifyDataSetChanged()
        }
    }

    private fun setNumberOfPendingItemsText(expandableCountryStateList : MutableList<ExpandableModel>){
        var numOfPendingItems = 0
        for(i in expandableCountryStateList){
            numOfPendingItems += i.header?.childrenList?.size ?: 0
        }
        pending_tv.text = getString(R.string.num_pending, numOfPendingItems.toString())
    }

}