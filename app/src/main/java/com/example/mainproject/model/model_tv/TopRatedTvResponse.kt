package com.example.mainproject.model.model_tv

import com.google.gson.annotations.SerializedName

data class TopRatedTvResponse(

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: List<TvTopRated>? = null,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
)