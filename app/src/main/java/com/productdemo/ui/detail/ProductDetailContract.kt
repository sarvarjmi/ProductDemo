package com.productdemo.ui.detail

import com.productdemo.domain.model.Product

data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ProductDetailIntent {
    data class LoadProduct(val id: Int) : ProductDetailIntent
}

sealed interface ProductDetailEffect {
    data class ShowError(val message: String) : ProductDetailEffect
}
