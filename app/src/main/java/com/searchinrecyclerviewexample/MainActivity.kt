package com.searchinrecyclerviewexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.searchinrecyclerviewexample.adapter.SearchImageAdapter
import com.searchinrecyclerviewexample.databinding.ActivityMainBinding
import com.searchinrecyclerviewexample.model.SearchImageRes

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchImageAdapter = SearchImageAdapter()
        binding.rvImages.adapter = searchImageAdapter
        searchImageAdapter.submitList(getSearchedImages())
    }

    private fun getSearchedImages(): MutableList<SearchImageRes>? {
        val searchedImageList = mutableListOf<SearchImageRes>()
        for (i in 0..87) {
            searchedImageList.add(
                SearchImageRes(
                    id = i,
                    views = 1000,
                    previewURL = "https://cdn.pixabay.com/photo/2013/10/15/09/12/flower-195893_150.jpg"
                )
            )
        }
        return searchedImageList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}