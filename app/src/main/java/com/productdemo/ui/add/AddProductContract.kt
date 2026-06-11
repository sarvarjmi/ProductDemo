package com.productdemo.ui.add

data class AddProductState(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val brand: String = "",
    val stock: String = "",
    val discountPercentage: String = "",
    val rating: String = "",
    val sku: String = "",
    val weight: String = "",
    val dimensionsWidth: String = "",
    val dimensionsHeight: String = "",
    val dimensionsDepth: String = "",
    val warrantyInformation: String = "",
    val shippingInformation: String = "",
    val availabilityStatus: String = "",
    val returnPolicy: String = "",
    val minimumOrderQuantity: String = "",
    val tags: String = "",
    val barcode: String = "",
    val qrCode: String = "",
    val thumbnail: String = "",
    val images: List<String> = emptyList(),
    val availableImages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed interface AddProductIntent {
    data class TitleChanged(val value: String) : AddProductIntent
    data class DescriptionChanged(val value: String) : AddProductIntent
    data class PriceChanged(val value: String) : AddProductIntent
    data class CategoryChanged(val value: String) : AddProductIntent
    data class BrandChanged(val value: String) : AddProductIntent
    data class StockChanged(val value: String) : AddProductIntent
    data class DiscountPercentageChanged(val value: String) : AddProductIntent
    data class RatingChanged(val value: String) : AddProductIntent
    data class SkuChanged(val value: String) : AddProductIntent
    data class WeightChanged(val value: String) : AddProductIntent
    data class DimensionsWidthChanged(val value: String) : AddProductIntent
    data class DimensionsHeightChanged(val value: String) : AddProductIntent
    data class DimensionsDepthChanged(val value: String) : AddProductIntent
    data class WarrantyInformationChanged(val value: String) : AddProductIntent
    data class ShippingInformationChanged(val value: String) : AddProductIntent
    data class AvailabilityStatusChanged(val value: String) : AddProductIntent
    data class ReturnPolicyChanged(val value: String) : AddProductIntent
    data class MinimumOrderQuantityChanged(val value: String) : AddProductIntent
    data class TagsChanged(val value: String) : AddProductIntent
    data class BarcodeChanged(val value: String) : AddProductIntent
    data class QrCodeChanged(val value: String) : AddProductIntent
    data class ThumbnailChanged(val value: String) : AddProductIntent

    data class ImageSelected(val imageUrl: String) : AddProductIntent
    data class ImageDeselected(val imageUrl: String) : AddProductIntent
    data object FillRandomData : AddProductIntent
    data object Submit : AddProductIntent
}

sealed interface AddProductEffect {
    data object NavigateBack : AddProductEffect
    data class ShowError(val message: String) : AddProductEffect
}
