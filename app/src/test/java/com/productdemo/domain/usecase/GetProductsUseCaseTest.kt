package com.productdemo.domain.usecase

import com.productdemo.domain.model.Dimensions
import com.productdemo.domain.model.Meta
import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {

    private lateinit var getProductsUseCase: GetProductsUseCase
    private val repository: ProductRepository = mockk()

    @Before
    fun setUp() {
        getProductsUseCase = GetProductsUseCase(repository)
    }

    @Test
    fun `invoke should return products from repository`() {
        runTest {
            // Given
            val products = listOf(
                Product(
                    id = 1, title = "Test Product", description = "Desc", category = "Cat",
                    price = 10.0, discountPercentage = 0.0, rating = 0.0, stock = 0,
                    tags = emptyList(), brand = null, sku = "", weight = 0,
                    dimensions = Dimensions(0.0, 0.0, 0.0), warrantyInformation = "",
                    shippingInformation = "", availabilityStatus = "", reviews = emptyList(),
                    returnPolicy = "", minimumOrderQuantity = 1, meta = Meta("", "", "", ""),
                    images = emptyList(), thumbnail = ""
                )
            )
            every { repository.getProducts() } returns flowOf(products)

            // When
            val result = getProductsUseCase().first()

            // Then
            assertEquals(products, result)
        }
    }

    @Test
    fun `invoke should return empty list when no products available`() {
        runTest {
            // Given
            val products = emptyList<Product>()
            every { repository.getProducts() } returns flowOf(products)

            // When
            val result = getProductsUseCase().first()

            // Then
            assertEquals(emptyList<Product>(), result)
        }
    }

    @Test
    fun `invoke should return multiple products`() {
        runTest {
            // Given
            val products = listOf(
                createTestProduct(id = 1, title = "Product 1"),
                createTestProduct(id = 2, title = "Product 2"),
                createTestProduct(id = 3, title = "Product 3")
            )
            every { repository.getProducts() } returns flowOf(products)

            // When
            val result = getProductsUseCase().first()

            // Then
            assertEquals(3, result.size)
            assertEquals("Product 1", result[0].title)
            assertEquals("Product 2", result[1].title)
            assertEquals("Product 3", result[2].title)
        }
    }

    private fun createTestProduct(
        id: Int,
        title: String = "Test Product",
        price: Double = 10.0,
        stock: Int = 5
    ): Product {
        return Product(
            id = id, title = title, description = "Test Description", category = "Test",
            price = price, discountPercentage = 0.0, rating = 4.5, stock = stock,
            tags = listOf("tag1", "tag2"), brand = "TestBrand", sku = "SKU-$id", weight = 1,
            dimensions = Dimensions(10.0, 10.0, 10.0), warrantyInformation = "1 year",
            shippingInformation = "Free shipping", availabilityStatus = "In Stock",
            reviews = emptyList(),
            returnPolicy = "30 days", minimumOrderQuantity = 1, meta = Meta("2024-01-01", "2024-01-01", "BC123", "QR123"),
            images = listOf("img1.jpg"), thumbnail = "thumb.jpg"
        )
    }
}

