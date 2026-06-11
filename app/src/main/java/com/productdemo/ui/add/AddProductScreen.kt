package com.productdemo.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.productdemo.ui.theme.ProductDemoTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddProductScreen(
    viewModel: AddProductViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddProductEffect.NavigateBack -> onBackClick()
                is AddProductEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add New Product") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { viewModel.onIntent(AddProductIntent.FillRandomData) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Fill Random Dummy Data")
            }

            Text(
                text = "General Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            ProductTextField(
                value = state.title,
                onValueChange = { viewModel.onIntent(AddProductIntent.TitleChanged(it)) },
                label = "Product Title *",
                isError = state.error != null && state.title.isBlank()
            )

            ProductTextField(
                value = state.brand,
                onValueChange = { viewModel.onIntent(AddProductIntent.BrandChanged(it)) },
                label = "Brand *",
                isError = state.error != null && state.brand.isBlank()
            )

            ProductTextField(
                value = state.category,
                onValueChange = { viewModel.onIntent(AddProductIntent.CategoryChanged(it)) },
                label = "Category *",
                isError = state.error != null && state.category.isBlank()
            )

            ProductTextField(
                value = state.description,
                onValueChange = { viewModel.onIntent(AddProductIntent.DescriptionChanged(it)) },
                label = "Description *",
                minLines = 3,
                isError = state.error != null && state.description.isBlank()
            )

            Text(
                text = "Pricing & Inventory",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProductTextField(
                    value = state.price,
                    onValueChange = { viewModel.onIntent(AddProductIntent.PriceChanged(it)) },
                    label = "Price ($) *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    isError = state.error != null && (state.price.isBlank() || state.price.toDoubleOrNull() == null)
                )
                ProductTextField(
                    value = state.discountPercentage,
                    onValueChange = { viewModel.onIntent(AddProductIntent.DiscountPercentageChanged(it)) },
                    label = "Discount % *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    isError = state.error != null && (state.discountPercentage.isBlank() || state.discountPercentage.toDoubleOrNull() == null)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProductTextField(
                    value = state.stock,
                    onValueChange = { viewModel.onIntent(AddProductIntent.StockChanged(it)) },
                    label = "Stock *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number,
                    isError = state.error != null && (state.stock.isBlank() || state.stock.toIntOrNull() == null)
                )
                ProductTextField(
                    value = state.rating,
                    onValueChange = { viewModel.onIntent(AddProductIntent.RatingChanged(it)) },
                    label = "Rating *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    isError = state.error != null && (state.rating.isBlank() || state.rating.toDoubleOrNull() == null)
                )
            }

            Text(
                text = "Specifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            ProductTextField(
                value = state.sku,
                onValueChange = { viewModel.onIntent(AddProductIntent.SkuChanged(it)) },
                label = "SKU *",
                isError = state.error != null && state.sku.isBlank()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProductTextField(
                    value = state.weight,
                    onValueChange = { viewModel.onIntent(AddProductIntent.WeightChanged(it)) },
                    label = "Weight *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number,
                    isError = state.error != null && (state.weight.isBlank() || state.weight.toIntOrNull() == null)
                )
                ProductTextField(
                    value = state.minimumOrderQuantity,
                    onValueChange = { viewModel.onIntent(AddProductIntent.MinimumOrderQuantityChanged(it)) },
                    label = "Min Order Qty *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number,
                    isError = state.error != null && (state.minimumOrderQuantity.isBlank() || state.minimumOrderQuantity.toIntOrNull() == null)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProductTextField(
                    value = state.dimensionsWidth,
                    onValueChange = { viewModel.onIntent(AddProductIntent.DimensionsWidthChanged(it)) },
                    label = "Width *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    isError = state.error != null && (state.dimensionsWidth.isBlank() || state.dimensionsWidth.toDoubleOrNull() == null)
                )
                ProductTextField(
                    value = state.dimensionsHeight,
                    onValueChange = { viewModel.onIntent(AddProductIntent.DimensionsHeightChanged(it)) },
                    label = "Height *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    isError = state.error != null && (state.dimensionsHeight.isBlank() || state.dimensionsHeight.toDoubleOrNull() == null)
                )
                ProductTextField(
                    value = state.dimensionsDepth,
                    onValueChange = { viewModel.onIntent(AddProductIntent.DimensionsDepthChanged(it)) },
                    label = "Depth *",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    isError = state.error != null && (state.dimensionsDepth.isBlank() || state.dimensionsDepth.toDoubleOrNull() == null)
                )
            }

            Text(
                text = "Shipping & Logistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            ProductTextField(
                value = state.warrantyInformation,
                onValueChange = { viewModel.onIntent(AddProductIntent.WarrantyInformationChanged(it)) },
                label = "Warranty Info *",
                isError = state.error != null && state.warrantyInformation.isBlank()
            )

            ProductTextField(
                value = state.shippingInformation,
                onValueChange = { viewModel.onIntent(AddProductIntent.ShippingInformationChanged(it)) },
                label = "Shipping Info *",
                isError = state.error != null && state.shippingInformation.isBlank()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProductTextField(
                    value = state.availabilityStatus,
                    onValueChange = { viewModel.onIntent(AddProductIntent.AvailabilityStatusChanged(it)) },
                    label = "Availability *",
                    modifier = Modifier.weight(1f),
                    isError = state.error != null && state.availabilityStatus.isBlank()
                )
                ProductTextField(
                    value = state.returnPolicy,
                    onValueChange = { viewModel.onIntent(AddProductIntent.ReturnPolicyChanged(it)) },
                    label = "Return Policy *",
                    modifier = Modifier.weight(1f),
                    isError = state.error != null && state.returnPolicy.isBlank()
                )
            }

            ProductTextField(
                value = state.tags,
                onValueChange = { viewModel.onIntent(AddProductIntent.TagsChanged(it)) },
                label = "Tags (comma separated) *",
                isError = state.error != null && state.tags.isBlank()
            )

            Text(
                text = "Metadata & Thumbnail",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            ProductTextField(
                value = state.barcode,
                onValueChange = { viewModel.onIntent(AddProductIntent.BarcodeChanged(it)) },
                label = "Barcode *",
                isError = state.error != null && state.barcode.isBlank()
            )

            ProductTextField(
                value = state.qrCode,
                onValueChange = { viewModel.onIntent(AddProductIntent.QrCodeChanged(it)) },
                label = "QR Code URL *",
                isError = state.error != null && state.qrCode.isBlank()
            )

            ProductTextField(
                value = state.thumbnail,
                onValueChange = { viewModel.onIntent(AddProductIntent.ThumbnailChanged(it)) },
                label = "Thumbnail URL *",
                isError = state.error != null && state.thumbnail.isBlank()
            )

            Text(
                text = "Product Images",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (state.images.isEmpty()) "Select Images *" else "${state.images.size} images selected",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Images") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                    isError = state.error != null && state.images.isEmpty()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.availableImages.forEach { imageUrl ->
                        val isSelected = state.images.contains(imageUrl)
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp).padding(4.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = imageUrl.substringAfterLast("/"),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            },
                            onClick = {
                                if (isSelected) {
                                    viewModel.onIntent(AddProductIntent.ImageDeselected(imageUrl))
                                } else {
                                    viewModel.onIntent(AddProductIntent.ImageSelected(imageUrl))
                                }
                            }
                        )
                    }
                }
            }

            if (state.images.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    maxItemsInEachRow = 3
                ) {
                    state.images.forEach { imageUrl ->
                        InputChip(
                            selected = true,
                            onClick = { viewModel.onIntent(AddProductIntent.ImageDeselected(imageUrl)) },
                            label = { 
                                Text(
                                    imageUrl.substringAfterLast("/"),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(max = 100.dp)
                                ) 
                            },
                            trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { viewModel.onIntent(AddProductIntent.Submit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                shape = MaterialTheme.shapes.medium,
                contentPadding = ButtonDefaults.ContentPadding.plus(PaddingValues(vertical = 12.dp))
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Submit Product", style = MaterialTheme.typography.titleMedium)
                }
            }
            
            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ProductTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        minLines = minLines,
        singleLine = minLines == 1,
        isError = isError
    )
}

@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    ProductDemoTheme {
        // Preview content
    }
}
