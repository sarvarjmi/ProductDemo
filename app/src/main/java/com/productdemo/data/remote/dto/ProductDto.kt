package com.productdemo.data.remote.dto

import com.productdemo.domain.model.Dimensions
import com.productdemo.domain.model.Meta
import com.productdemo.domain.model.Product
import com.productdemo.domain.model.Review
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponseDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String>,
    val brand: String? = null,
    val sku: String,
    val weight: Int,
    val dimensions: DimensionsDto,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: List<ReviewDto>,
    val returnPolicy: String,
    val minimumOrderQuantity: Int,
    val meta: MetaDto,
    val images: List<String>,
    val thumbnail: String
)

@JsonClass(generateAdapter = true)
data class DimensionsDto(
    val width: Double,
    val height: Double,
    val depth: Double
)

@JsonClass(generateAdapter = true)
data class ReviewDto(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)

@JsonClass(generateAdapter = true)
data class MetaDto(
    val createdAt: String,
    val updatedAt: String,
    val barcode: String,
    val qrCode: String
)

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        dimensions = dimensions.toDomain(),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { it.toDomain() },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = meta.toDomain(),
        images = images,
        thumbnail = thumbnail
    )
}

fun DimensionsDto.toDomain(): Dimensions = Dimensions(width, height, depth)
fun ReviewDto.toDomain(): Review = Review(rating, comment, date, reviewerName, reviewerEmail)
fun MetaDto.toDomain(): Meta = Meta(createdAt, updatedAt, barcode, qrCode)

fun Product.toDto(): ProductDto {
    return ProductDto(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        dimensions = DimensionsDto(dimensions.width, dimensions.height, dimensions.depth),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { ReviewDto(it.rating, it.comment, it.date, it.reviewerName, it.reviewerEmail) },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = MetaDto(meta.createdAt, meta.updatedAt, meta.barcode, meta.qrCode),
        images = images,
        thumbnail = thumbnail
    )
}
