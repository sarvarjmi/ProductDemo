package com.productdemo.domain.usecase

import com.productdemo.domain.model.Dimensions
import com.productdemo.domain.model.Meta
import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import com.productdemo.util.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchProductsUseCaseTest {

    private lateinit var searchProductsUseCase: SearchProductsUseCase
    private val repository: ProductRepository = mockk()

    @Before
    fun setUp() {
        searchProductsUseCase = SearchProductsUseCase(repository)
    }

    @Test
    fun `invoke should return search results for query`() {
        runTest {
            // Given
            val query = "laptop"
            val products = listOf(
                createTestProduct(id = 1, title = "Gaming Laptop"),
                createTestProduct(id = 2, title = "Business Laptop")
            )
            every { repository.searchProducts(query) } returns flowOf(products)

            // When
            val result = searchProductsUseCase.invoke(query).first()

            // Then
            assertEquals(2, result.size)
            assertEquals("Gaming Laptop", result[0].title)
            assertEquals("Business Laptop", result[1].title)
        }
    }

    @Test
    fun `invoke should return empty list for no matches`() {
        runTest {
            // Given
            val query = "nonexistent"
            every { repository.searchProducts(query) } returns flowOf(emptyList())

            // When
            val result = searchProductsUseCase.invoke(query).first()

            // Then
            assertEquals(emptyList<Product>(), result)
        }
    }

    @Test
    fun `invoke should handle different search queries`() {
        runTest {
            // Given
            val query1 = "phone"
            val query2 = "tablet"
            val products1 = listOf(createTestProduct(id = 1, title = "iPhone"))
            val products2 = listOf(createTestProduct(id = 2, title = "iPad"))

            every { repository.searchProducts(query1) } returns flowOf(products1)
            every { repository.searchProducts(query2) } returns flowOf(products2)

            // When
            val result1 = searchProductsUseCase.invoke(query1).first()
            val result2 = searchProductsUseCase.invoke(query2).first()

            // Then
            assertEquals(1, result1.size)
            assertEquals("iPhone", result1[0].title)
            assertEquals(1, result2.size)
            assertEquals("iPad", result2[0].title)
        }
    }

    @Test
    fun `fetch should return success when search fetch succeeds`() {
        runTest {
            // Given
            val query = "laptop"
            val limit = 10
            val skip = 0
            coEvery {
                repository.fetchAndCacheSearch(query, limit, skip)
            } returns Result.success(Unit)

            // When
            val result = searchProductsUseCase.fetch(query, limit, skip)

            // Then
            assertTrue(result is Resource.Success)
            assertEquals(Unit, (result as Resource.Success).data)
        }
    }

    @Test
    fun `fetch should return error when search fetch fails`() {
        runTest {
            // Given
            val query = "laptop"
            val limit = 10
            val skip = 0
            val exception = Exception("Network error")
            coEvery {
                repository.fetchAndCacheSearch(query, limit, skip)
            } returns Result.failure(exception)

            // When
            val result = searchProductsUseCase.fetch(query, limit, skip)

            // Then
            assertTrue(result is Resource.Error)
            assertEquals("Network error", (result as Resource.Error).message)
        }
    }

    private fun createTestProduct(
        id: Int,
        title: String = "Test Product",
        price: Double = 10.0,
        stock: Int = 5
    ): Product {
        return Product(
            id = id,
            title = title,
            description = "Test Description",
            category = "Test",
            price = price,
            discountPercentage = 0.0,
            rating = 4.5,
            stock = stock,
            tags = listOf("tag1", "tag2"),
            brand = "TestBrand",
            sku = "SKU-$id",
            weight = 1,
            dimensions = Dimensions(10.0, 10.0, 10.0),
            warrantyInformation = "1 year",
            shippingInformation = "Free shipping",
            availabilityStatus = "In Stock",
            reviews = emptyList(),
            returnPolicy = "30 days",
            minimumOrderQuantity = 1,
            meta = Meta("2024-01-01", "2024-01-01", "BC123", "QR123"),
            images = listOf("img1.jpg"),
            thumbnail = "thumb.jpg"
        )
    }
}

