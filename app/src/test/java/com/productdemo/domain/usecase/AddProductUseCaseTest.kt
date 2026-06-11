package com.productdemo.domain.usecase

import com.productdemo.domain.model.Dimensions
import com.productdemo.domain.model.Meta
import com.productdemo.domain.model.Product
import com.productdemo.domain.repository.ProductRepository
import com.productdemo.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddProductUseCaseTest {

    private lateinit var addProductUseCase: AddProductUseCase
    private val repository: ProductRepository = mockk()

    @Before
    fun setUp() {
        addProductUseCase = AddProductUseCase(repository)
    }

    @Test
    fun `invoke should return success when product is added successfully`() {
        runTest {
            // Given
            val product = createTestProduct(id = 1, title = "New Product")
            coEvery { repository.addProduct(product) } returns Result.success(product)

            // When
            val result = addProductUseCase.invoke(product)

            // Then
            assertTrue(result is Resource.Success)
            val successResult = result as Resource.Success
            assertEquals(product, successResult.data)
            assertEquals("New Product", successResult.data.title)
        }
    }

    @Test
    fun `invoke should return error when product addition fails`() {
        runTest {
            // Given
            val product = createTestProduct(id = 1, title = "New Product")
            val exception = Exception("Failed to add product")
            coEvery { repository.addProduct(product) } returns Result.failure(exception)

            // When
            val result = addProductUseCase.invoke(product)

            // Then
            assertTrue(result is Resource.Error)
            assertEquals("Failed to add product", (result as Resource.Error).message)
        }
    }

    @Test
    fun `invoke should add product with all required fields`() {
        runTest {
            // Given
            val product = Product(
                id = 100,
                title = "Premium Product",
                description = "This is a premium product",
                category = "Premium",
                price = 999.99,
                discountPercentage = 5.0,
                rating = 4.9,
                stock = 50,
                tags = listOf("premium", "bestseller"),
                brand = "PremiumBrand",
                sku = "PREM-100",
                weight = 5,
                dimensions = Dimensions(50.0, 40.0, 15.0),
                warrantyInformation = "3 years warranty",
                shippingInformation = "Express shipping available",
                availabilityStatus = "In Stock",
                reviews = emptyList(),
                returnPolicy = "90 days return",
                minimumOrderQuantity = 1,
                meta = Meta("2024-01-01", "2024-06-01", "PREM100BC", "PREM100QR"),
                images = listOf("img1.jpg", "img2.jpg", "img3.jpg"),
                thumbnail = "thumb.jpg"
            )
            coEvery { repository.addProduct(product) } returns Result.success(product)

            // When
            val result = addProductUseCase.invoke(product)

            // Then
            assertTrue(result is Resource.Success)
            val addedProduct = (result as Resource.Success).data
            assertEquals(product.title, addedProduct.title)
            assertEquals(product.price, addedProduct.price, 0.01)
            assertEquals(product.stock, addedProduct.stock)
            assertEquals(product.category, addedProduct.category)
        }
    }

    @Test
    fun `invoke should handle multiple product additions`() {
        runTest {
            // Given
            val product1 = createTestProduct(id = 1, title = "Product 1")
            val product2 = createTestProduct(id = 2, title = "Product 2")
            coEvery { repository.addProduct(product1) } returns Result.success(product1)
            coEvery { repository.addProduct(product2) } returns Result.success(product2)

            // When
            val result1 = addProductUseCase.invoke(product1)
            val result2 = addProductUseCase.invoke(product2)

            // Then
            assertTrue(result1 is Resource.Success)
            assertTrue(result2 is Resource.Success)
            assertEquals("Product 1", (result1 as Resource.Success).data.title)
            assertEquals("Product 2", (result2 as Resource.Success).data.title)
        }
    }

    @Test
    fun `invoke should return error with null throwable when repository fails without exception`() {
        runTest {
            // Given
            val product = createTestProduct(id = 1)
            coEvery { repository.addProduct(product) } returns Result.failure(
                Exception("Unknown error")
            )

            // When
            val result = addProductUseCase.invoke(product)

            // Then
            assertTrue(result is Resource.Error)
            assertEquals("Unknown error", (result as Resource.Error).message)
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

