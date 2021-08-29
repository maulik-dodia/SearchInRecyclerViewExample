package com.searchinrecyclerviewexample.model

data class SearchImageRes(
    val total: Int,
    val totalHits: Int,
    val hits: ArrayList<Hit>
)
