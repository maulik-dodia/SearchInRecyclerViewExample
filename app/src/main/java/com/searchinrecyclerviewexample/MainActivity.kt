package com.searchinrecyclerviewexample

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.searchinrecyclerviewexample.adapter.SearchImageAdapter
import com.searchinrecyclerviewexample.databinding.ActivityMainBinding
import com.searchinrecyclerviewexample.model.BaseRes
import com.searchinrecyclerviewexample.model.Hit
import com.searchinrecyclerviewexample.utils.SEVEN_HUNDRED
import com.searchinrecyclerviewexample.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getResultsFromAPI()

        val searchImageAdapter = SearchImageAdapter()
        binding.apply {
            rvImages.apply {
                adapter = searchImageAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            viewModel.getResultsFromAPI()
                        }
                    }
                })
            }

            edtSearch.addTextChangedListener(
                DebouncingQueryTextListener(lifecycle) { newText ->
                    newText?.let {
                        viewModel.apply {
                            nextPageNo = 0
                            searchedTerm = it
                            getResultsFromAPI()
                        }
                    }
                }
            )
        }
        viewModel.resultsLiveData.observe(this) { response ->
            when (response) {
                is BaseRes.Loading -> {
                    showProgressBar()
                }
                is BaseRes.Success -> {
                    hideProgressBar()
                    response.data?.let { successRes ->
                        searchImageAdapter.submitList(successRes as ArrayList<Hit>)
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

    internal class DebouncingQueryTextListener(
        lifecycle: Lifecycle,
        private val onDebouncingQueryTextChange: (String?) -> Unit
    ) : TextWatcher {

        private var searchJob: Job? = null
        private val coroutineScope = lifecycle.coroutineScope

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //
        }

        override fun afterTextChanged(s: Editable?) {
            val newText = s.toString()
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                newText.let {
                    delay(SEVEN_HUNDRED)
                    onDebouncingQueryTextChange(newText)
                }
            }
        }
    }
}