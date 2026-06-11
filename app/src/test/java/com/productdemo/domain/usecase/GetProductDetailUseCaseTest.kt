package com.productdemo.domain.usecase

import com.productdemo.domain.model.Dimensions
import com.productdemo.domain.model.Meta
import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetProductDetailUseCaseTest {

    private lateinit var getProductDetailUseCase: GetProductDetailUseCase
    private val repository: ProductRepository = mockk()

    @Before
    fun setUp() {
        getProductDetailUseCase = GetProductDetailUseCase(repository)
    }

    @Test
    fun `invoke should return product detail by id`() {
        runTest {
            // Given
            val productId = 1
            val product = createTestProduct(id = productId, title = "Product 1")
            coEvery { repository.getProductById(productId) } returns flowOf(product)

            // When
            val result = getProductDetailUseCase.invoke(productId).first()

            // Then
            assertEquals(product, result)
            assertEquals(productId, result?.id)
            assertEquals("Product 1", result?.title)
        }
    }

    @Test
    fun `invoke should return null when product not found`() {
        runTest {
            // Given
            val productId = 999
            coEvery { repository.getProductById(productId) } returns flowOf(null)

            // When
            val result = getProductDetailUseCase.invoke(productId).first()

            // Then
            assertNull(result)
        }
    }

    @Test
    fun `invoke should return correct product with all details`() {
        runTest {
            // Given
            val productId = 42
            val product = Product(
                id = productId,
                title = "Premium Laptop",
                description = "High performance laptop",
                category = "Electronics",
                price = 999.99,
                discountPercentage = 10.0,
                rating = 4.8,
                stock = 15,
                tags = listOf("laptop", "electronics", "premium"),
                brand = "TechBrand",
                sku = "LAP-001",
                weight = 2,
                dimensions = Dimensions(35.0, 25.0, 2.0),
                warrantyInformation = "2 years warranty",
                shippingInformation = "Free worldwide shipping",
                availabilityStatus = "In Stock",
                reviews = emptyList(),
                returnPolicy = "60 days return",
                minimumOrderQuantity = 1,
                meta = Meta("2024-01-01", "2024-06-01", "LAP001BC", "LAP001QR"),
                images = listOf("img1.jpg", "img2.jpg"),
                thumbnail = "thumb.jpg"
            )
            coEvery { repository.getProductById(productId) } returns flowOf(product)

            // When
            val result = getProductDetailUseCase.invoke(productId).first()

            // Then
            assertEquals(product, result)
            result?.apply {
                assertEquals("Premium Laptop", title)
                assertEquals(999.99, price, 0.01)
                assertEquals(4.8, rating, 0.1)
                assertEquals("Electronics", category)
                assertEquals(15, stock)
            }
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

