package com.searchinrecyclerviewexample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchinrecyclerviewexample.model.BaseRes
import com.searchinrecyclerviewexample.model.Hit
import com.searchinrecyclerviewexample.network.RetrofitInstance
import com.searchinrecyclerviewexample.utils.API_KEY
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var nextPageNo = 0
    var searchedTerm: String? = null
    private val searchedImageList = ArrayList<Hit>()
    var resultsLiveData: MutableLiveData<BaseRes<String>> = MutableLiveData()

    fun getResultsFromAPI() {
        viewModelScope.launch {
            resultsLiveData.postValue(BaseRes.Loading())
            try {
                val response =
                    RetrofitInstance.api.getData(
                        apiKey = API_KEY,
                        searchedTerm = if (searchedTerm?.trim()
                                .isNullOrEmpty()
                        ) null else searchedTerm?.trim(),
                        nextPageNo = ++nextPageNo
                    )
                if (response.isSuccessful) {
                    response.body()?.let { searchImageResponse ->
                        if (nextPageNo == 1) {
                            searchedImageList.clear()
                        }
                        searchedImageList.addAll(searchImageResponse.hits)
                        val newList = ArrayList<Hit>(searchedImageList)
                        resultsLiveData.postValue(BaseRes.Success(newList))
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