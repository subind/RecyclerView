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
        for (childList in transaction.transactionList) {
            expandableCountryList.add(ExpandableModel(ExpandableModel.HEADER, childList))
        }
        return expandableCountryList
    }

}