package com.productdemo.data.remote

import com.productdemo.data.remote.dto.ProductDto
import com.productdemo.data.remote.dto.ProductResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): ProductResponseDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): ProductResponseDto

    @GET("products/{id}")
    suspend fun getProductDetails(
        @Path("id") id: Int
    ): ProductDto

    @POST("products/add")
    suspend fun addProduct(
        @Body product: ProductDto
    ): ProductDto

    companion object {
        const val BASE_URL = "https://dummyjson.com/"
    }
}
