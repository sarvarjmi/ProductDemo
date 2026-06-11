package com.productdemo.data.repository

import com.productdemo.data.local.dao.ProductDao
import com.productdemo.data.local.entity.toDomain
import com.productdemo.data.local.entity.toEntity
import com.productdemo.data.remote.ProductApiService
import com.productdemo.data.remote.dto.toDomain
import com.productdemo.data.remote.dto.toDto
import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService,
    private val dao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return dao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshProducts(limit: Int, skip: Int): Result<Unit> {
        return try {
            val response = api.getProducts(limit, skip)
            val products = response.products.map { it.toDomain() }
            dao.insertProducts(products.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return dao.searchProducts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun fetchAndCacheSearch(query: String, limit: Int, skip: Int): Result<Unit> {
        return try {
            val response = api.searchProducts(query, limit, skip)
            val products = response.products.map { it.toDomain() }
            dao.insertProducts(products.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<Product?> {
        return try {
            // Check local first
            val localProduct = dao.getProductById(id)
            if (localProduct != null) {
                return Result.success(localProduct.toDomain())
            }
            
            // Try refresh from remote
            try {
                val remoteProduct = api.getProductDetails(id)
                val domainProduct = remoteProduct.toDomain()
                dao.insertProduct(domainProduct.toEntity())
                Result.success(domainProduct)
            } catch (e: Exception) {
                // If remote fails and no local data, return failure
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addProduct(product: Product): Result<Product> {
        return try {
            val response = api.addProduct(product.toDto())
            val domainProduct = response.toDomain()
            dao.insertProduct(domainProduct.toEntity())
            Result.success(domainProduct)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
