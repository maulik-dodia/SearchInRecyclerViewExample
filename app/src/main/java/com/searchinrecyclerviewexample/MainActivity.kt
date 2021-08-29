package com.searchinrecyclerviewexample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.searchinrecyclerviewexample.adapter.SearchImageAdapter
import com.searchinrecyclerviewexample.databinding.ActivityMainBinding
import com.searchinrecyclerviewexample.model.BaseRes
import com.searchinrecyclerviewexample.model.Hit
import com.searchinrecyclerviewexample.model.SearchImageRes
import com.searchinrecyclerviewexample.viewmodel.MainViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val searchedImageList = ArrayList<Hit>()
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getResultsFromAPI()

        val searchImageAdapter = SearchImageAdapter()
        binding.rvImages.apply {
            adapter = searchImageAdapter
        }
        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.getResultsFromAPI()
                }
            }
        })

        viewModel.resultsLiveData.observe(this) { response ->
            when (response) {
                is BaseRes.Loading -> {
                    showProgressBar()
                }
                is BaseRes.Success -> {
                    hideProgressBar()
                    response.data?.let { successRes ->
                        searchedImageList.addAll((successRes as SearchImageRes).hits)
                        val newList = ArrayList<Hit>(searchedImageList)
                        searchImageAdapter.submitList(newList)
                    }
                }
                is BaseRes.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Timber.d(message)
                        Toast.makeText(
                            this,
                            String.format(getString(R.string.str_error_occurred), message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.GONE
            rvImages.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}