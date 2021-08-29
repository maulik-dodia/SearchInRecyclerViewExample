package com.searchinrecyclerviewexample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchinrecyclerviewexample.model.BaseRes
import com.searchinrecyclerviewexample.network.RetrofitInstance
import com.searchinrecyclerviewexample.utils.API_KEY
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var nextPageNo = 0
    val resultsLiveData: MutableLiveData<BaseRes<String>> = MutableLiveData()

    fun getResultsFromAPI() {
        nextPageNo++
        viewModelScope.launch {
            resultsLiveData.postValue(BaseRes.Loading())
            try {
                val response =
                    RetrofitInstance.api.getData(apiKey = API_KEY, nextPageNo = nextPageNo)
                if (response.isSuccessful) {
                    response.body()?.let { searchImageResponse ->
                        resultsLiveData.postValue(BaseRes.Success(searchImageResponse))
                    }
                } else {
                    resultsLiveData.postValue(BaseRes.Error(response.message()))
                }
            } catch (throwableError: Throwable) {
                throwableError.localizedMessage?.let {
                    resultsLiveData.postValue(BaseRes.Error(it))
                }
            }
        }
    }
}