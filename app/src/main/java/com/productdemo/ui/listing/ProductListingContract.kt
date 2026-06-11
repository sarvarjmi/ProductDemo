package com.productdemo.ui.listing

import com.productdemo.domain.model.Product

data class ProductListingState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null,
    val isEndReached: Boolean = false,
    val skip: Int = 0
)

sealed interface ProductListingIntent {
    data object LoadNextPage : ProductListingIntent
    data class Search(val query: String) : ProductListingIntent
    data object Refresh : ProductListingIntent
}

sealed interface ProductListingEffect {
    data class ShowError(val message: String) : ProductListingEffect
}
