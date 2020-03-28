package com.example.mainproject.model.multisearch

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsSearch>? = null,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
)