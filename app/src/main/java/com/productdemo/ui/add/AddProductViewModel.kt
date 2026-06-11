package com.productdemo.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productdemo.domain.model.Dimensions
import com.productdemo.domain.model.Meta
import com.productdemo.domain.model.Product
import com.productdemo.domain.usecase.AddProductUseCase
import com.productdemo.domain.usecase.GetProductsUseCase
import com.productdemo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val addProductUseCase: AddProductUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddProductState())
    val state: StateFlow<AddProductState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddProductEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadAvailableImages()
    }

    private fun loadAvailableImages() {
        viewModelScope.launch {
            getProductsUseCase().collect { products ->
                if (products.isNotEmpty()) {
                    val images = products.flatMap { it.images }.distinct()
                    _state.update { it.copy(availableImages = images) }
                }
            }
        }
    }

    fun onIntent(intent: AddProductIntent) {
        when (intent) {
            is AddProductIntent.TitleChanged -> _state.update { it.copy(title = intent.value) }
            is AddProductIntent.DescriptionChanged -> _state.update { it.copy(description = intent.value) }
            is AddProductIntent.PriceChanged -> _state.update { it.copy(price = intent.value) }
            is AddProductIntent.CategoryChanged -> _state.update { it.copy(category = intent.value) }
            is AddProductIntent.BrandChanged -> _state.update { it.copy(brand = intent.value) }
            is AddProductIntent.StockChanged -> _state.update { it.copy(stock = intent.value) }
            is AddProductIntent.DiscountPercentageChanged -> _state.update { it.copy(discountPercentage = intent.value) }
            is AddProductIntent.RatingChanged -> _state.update { it.copy(rating = intent.value) }
            is AddProductIntent.SkuChanged -> _state.update { it.copy(sku = intent.value) }
            is AddProductIntent.WeightChanged -> _state.update { it.copy(weight = intent.value) }
            is AddProductIntent.DimensionsWidthChanged -> _state.update { it.copy(dimensionsWidth = intent.value) }
            is AddProductIntent.DimensionsHeightChanged -> _state.update { it.copy(dimensionsHeight = intent.value) }
            is AddProductIntent.DimensionsDepthChanged -> _state.update { it.copy(dimensionsDepth = intent.value) }
            is AddProductIntent.WarrantyInformationChanged -> _state.update { it.copy(warrantyInformation = intent.value) }
            is AddProductIntent.ShippingInformationChanged -> _state.update { it.copy(shippingInformation = intent.value) }
            is AddProductIntent.AvailabilityStatusChanged -> _state.update { it.copy(availabilityStatus = intent.value) }
            is AddProductIntent.ReturnPolicyChanged -> _state.update { it.copy(returnPolicy = intent.value) }
            is AddProductIntent.MinimumOrderQuantityChanged -> _state.update { it.copy(minimumOrderQuantity = intent.value) }
            is AddProductIntent.TagsChanged -> _state.update { it.copy(tags = intent.value) }
            is AddProductIntent.BarcodeChanged -> _state.update { it.copy(barcode = intent.value) }
            is AddProductIntent.QrCodeChanged -> _state.update { it.copy(qrCode = intent.value) }
            is AddProductIntent.ThumbnailChanged -> _state.update { it.copy(thumbnail = intent.value) }
            
            is AddProductIntent.ImageSelected -> {
                _state.update { s ->
                    if (!s.images.contains(intent.imageUrl)) {
                        s.copy(images = s.images + intent.imageUrl)
                    } else s
                }
            }
            is AddProductIntent.ImageDeselected -> {
                _state.update { s ->
                    s.copy(images = s.images - intent.imageUrl)
                }
            }
            AddProductIntent.FillRandomData -> fillRandomData()
            AddProductIntent.Submit -> submitProduct()
        }
    }

    private fun fillRandomData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            var products = getProductsUseCase().first()

            // Only fetch from remote if local cache is empty
            if (products.isEmpty()) {
                getProductsUseCase.refresh(30, 0)
                products = getProductsUseCase().first()
            }

            if (products.isNotEmpty()) {
                val p = products[kotlin.random.Random.nextInt(products.size)]
                _state.update {
                    it.copy(
                        title = p.title,
                        description = p.description,
                        category = p.category,
                        price = p.price.toString(),
                        brand = p.brand ?: "",
                        stock = p.stock.toString(),
                        discountPercentage = p.discountPercentage.toString(),
                        rating = p.rating.toString(),
                        sku = p.sku,
                        weight = p.weight.toString(),
                        dimensionsWidth = p.dimensions.width.toString(),
                        dimensionsHeight = p.dimensions.height.toString(),
                        dimensionsDepth = p.dimensions.depth.toString(),
                        warrantyInformation = p.warrantyInformation,
                        shippingInformation = p.shippingInformation,
                        availabilityStatus = p.availabilityStatus,
                        returnPolicy = p.returnPolicy,
                        minimumOrderQuantity = p.minimumOrderQuantity.toString(),
                        tags = p.tags.joinToString(", "),
                        barcode = p.meta.barcode,
                        qrCode = p.meta.qrCode,
                        thumbnail = p.thumbnail,
                        images = listOfNotNull(p.images.firstOrNull()),
                        isLoading = false,
                        error = null
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false, error = "Could not fetch dummy data") }
            }
        }
    }

    private fun submitProduct() {
        val s = _state.value
        
        val validationError = when {
            s.title.isBlank() -> "Title is required"
            s.description.isBlank() -> "Description is required"
            s.category.isBlank() -> "Category is required"
            s.price.isBlank() || s.price.toDoubleOrNull() == null -> "Valid Price is required"
            s.brand.isBlank() -> "Brand is required"
            s.stock.isBlank() || s.stock.toIntOrNull() == null -> "Valid Stock is required"
            s.discountPercentage.isBlank() || s.discountPercentage.toDoubleOrNull() == null -> "Valid Discount is required"
            s.rating.isBlank() || s.rating.toDoubleOrNull() == null -> "Valid Rating is required"
            s.sku.isBlank() -> "SKU is required"
            s.weight.isBlank() || s.weight.toIntOrNull() == null -> "Valid Weight is required"
            s.dimensionsWidth.isBlank() || s.dimensionsWidth.toDoubleOrNull() == null -> "Valid Width is required"
            s.dimensionsHeight.isBlank() || s.dimensionsHeight.toDoubleOrNull() == null -> "Valid Height is required"
            s.dimensionsDepth.isBlank() || s.dimensionsDepth.toDoubleOrNull() == null -> "Valid Depth is required"
            s.warrantyInformation.isBlank() -> "Warranty Information is required"
            s.shippingInformation.isBlank() -> "Shipping Information is required"
            s.availabilityStatus.isBlank() -> "Availability Status is required"
            s.returnPolicy.isBlank() -> "Return Policy is required"
            s.minimumOrderQuantity.isBlank() || s.minimumOrderQuantity.toIntOrNull() == null -> "Valid Min Order Qty is required"
            s.tags.isBlank() -> "Tags are required"
            s.barcode.isBlank() -> "Barcode is required"
            s.qrCode.isBlank() -> "QR Code is required"
            s.thumbnail.isBlank() -> "Thumbnail is required"
            s.images.isEmpty() -> "At least one image is required"
            else -> null
        }

        if (validationError != null) {
            _state.update { it.copy(error = validationError) }
            viewModelScope.launch { _effect.emit(AddProductEffect.ShowError(validationError)) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val now = sdf.format(Date())
            val newProduct = Product(
                id = 0,
                title = s.title,
                description = s.description,
                category = s.category,
                price = s.price.toDouble(),
                discountPercentage = s.discountPercentage.toDouble(),
                rating = s.rating.toDouble(),
                stock = s.stock.toInt(),
                tags = s.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                brand = s.brand,
                sku = s.sku,
                weight = s.weight.toInt(),
                dimensions = Dimensions(
                    s.dimensionsWidth.toDouble(),
                    s.dimensionsHeight.toDouble(),
                    s.dimensionsDepth.toDouble()
                ),
                warrantyInformation = s.warrantyInformation,
                shippingInformation = s.shippingInformation,
                availabilityStatus = s.availabilityStatus,
                reviews = emptyList(),
                returnPolicy = s.returnPolicy,
                minimumOrderQuantity = s.minimumOrderQuantity.toInt(),
                meta = Meta(now, now, s.barcode, s.qrCode),
                images = s.images,
                thumbnail = s.thumbnail
            )

            val resource = addProductUseCase(newProduct)
            when (resource) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                    _effect.emit(AddProductEffect.NavigateBack)
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = resource.message) }
                    _effect.emit(AddProductEffect.ShowError(resource.message))
                }
                Resource.Loading -> {}
            }
        }
    }
}
