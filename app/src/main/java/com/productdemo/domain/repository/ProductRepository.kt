package com.productdemo.domain.repository

import com.productdemo.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun refreshProducts(limit: Int, skip: Int): Result<Unit>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun fetchAndCacheSearch(query: String, limit: Int, skip: Int): Result<Unit>
    suspend fun getProductById(id: Int): Result<Product?>
    suspend fun addProduct(product: Product): Result<Product>
}
