package com.productdemo.domain.usecase

import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import com.productdemo.util.Resource
import com.productdemo.util.toResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getProducts()
    }

    suspend fun refresh(limit: Int, skip: Int): Resource<Unit> {
        return repository.refreshProducts(limit, skip).toResource()
    }
}
