package com.productdemo.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.productdemo.domain.model.Dimensions
import com.productdemo.domain.model.Meta
import com.productdemo.domain.model.Product
import com.productdemo.domain.model.Review

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String>,
    val brand: String?,
    val sku: String,
    val weight: Int,
    @Embedded(prefix = "dim_") val dimensions: DimensionsEntity,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: List<ReviewEntity>,
    val returnPolicy: String,
    val minimumOrderQuantity: Int,
    @Embedded(prefix = "meta_") val meta: MetaEntity,
    val images: List<String>,
    val thumbnail: String
)

data class DimensionsEntity(
    val width: Double,
    val height: Double,
    val depth: Double
)

data class ReviewEntity(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)

data class MetaEntity(
    val createdAt: String,
    val updatedAt: String,
    val barcode: String,
    val qrCode: String
)

fun ProductEntity.toDomain(): Product {
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
        dimensions = Dimensions(dimensions.width, dimensions.height, dimensions.depth),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { Review(it.rating, it.comment, it.date, it.reviewerName, it.reviewerEmail) },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = Meta(meta.createdAt, meta.updatedAt, meta.barcode, meta.qrCode),
        images = images,
        thumbnail = thumbnail
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
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
        dimensions = DimensionsEntity(dimensions.width, dimensions.height, dimensions.depth),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { ReviewEntity(it.rating, it.comment, it.date, it.reviewerName, it.reviewerEmail) },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = MetaEntity(meta.createdAt, meta.updatedAt, meta.barcode, meta.qrCode),
        images = images,
        thumbnail = thumbnail
    )
}
