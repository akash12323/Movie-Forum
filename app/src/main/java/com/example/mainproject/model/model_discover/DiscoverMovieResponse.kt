package com.example.mainproject.model.model_discover

import com.google.gson.annotations.SerializedName

data class DiscoverMovieResponse(

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: List<DiscoveredMovies>? = null,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
)