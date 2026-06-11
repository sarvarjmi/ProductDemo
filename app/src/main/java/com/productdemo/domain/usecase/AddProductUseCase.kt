package com.productdemo.domain.usecase

import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import com.productdemo.util.Resource
import com.productdemo.util.toResource
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Resource<Product> {
        return repository.addProduct(product).toResource()
    }
}
