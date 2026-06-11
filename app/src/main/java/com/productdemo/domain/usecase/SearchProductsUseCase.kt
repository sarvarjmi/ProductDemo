package com.productdemo.domain.usecase

import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import com.productdemo.util.Resource
import com.productdemo.util.toResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(query: String): Flow<List<Product>> {
        return repository.searchProducts(query)
    }

    suspend fun fetch(query: String, limit: Int, skip: Int): Resource<Unit> {
        return repository.fetchAndCacheSearch(query, limit, skip).toResource()
    }
}
