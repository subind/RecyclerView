package com.example.recyclerviewpocs

import androidx.lifecycle.*
import com.example.recyclerviewpocs.models.ExpandableModel
import com.example.recyclerviewpocs.models.Transaction
import com.example.recyclerviewpocs.repository.MyRepository
import com.example.recyclerviewpocs.utils.ResultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(private val dispatcher: CoroutineDispatcher, private var myRepository: MyRepository?) : ViewModel(),
    LifecycleObserver {
    private val LOG_TAG = "CountryStateViewModel"
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    private val _obtainCountryStatesResponse= MutableLiveData<ResultOf<Transaction>>()
    val  obtainCountryStatesResponse: LiveData<ResultOf<Transaction>> = _obtainCountryStatesResponse
    private val iconList = mutableListOf<Int>()

    fun obtainCountryStateCapitals(){
        loading.postValue(true)

        viewModelScope.launch(dispatcher){
            var errorCode = -1
            try{
                var stateCapitalResponse =  myRepository?.fetchData()
                stateCapitalResponse?.let {
                    loading.postValue(false)
                    _obtainCountryStatesResponse.postValue(ResultOf.Success(it))
                }

            }catch (e : Exception){
                loading.postValue(false)
                e.printStackTrace()
                if(errorCode != -1){
                    _obtainCountryStatesResponse.postValue(ResultOf.Failure("Failed with Error Code ${errorCode} ", e))
                }else{
                    _obtainCountryStatesResponse.postValue(ResultOf.Failure("Failed with Exception ${e.message} ", e))
                }
            }
        }
    }

    fun prepareDataForExpandableAdapter(transaction: Transaction) : MutableList<ExpandableModel>{
        var expandableCountryList = mutableListOf<ExpandableModel>()
        for (childList in transaction.transactionList.withIndex()) {
            var expandableModel = ExpandableModel()
            expandableModel.type = ExpandableModel.HEADER
            expandableModel.header = childList.value
            expandableModel.drawable = iconList[childList.index]
            expandableCountryList.add(expandableModel)
        }
        return expandableCountryList
    }

    private fun prepareIconList() {
        iconList.add(0, R.drawable.pen_req)
        iconList.add(1, R.drawable.tra_req)
        iconList.add(2, R.drawable.acc_req)
        iconList.add(3, R.drawable.ser_req)
    }

    init {
        prepareIconList()
    }

}