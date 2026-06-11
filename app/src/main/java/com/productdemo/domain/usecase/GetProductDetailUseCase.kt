package com.productdemo.domain.usecase

import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import com.productdemo.util.Resource
import com.productdemo.util.toResource
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Resource<Product?> {
        return repository.getProductById(id).toResource()
    }
}
