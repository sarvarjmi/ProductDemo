package com.productdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.productdemo.ui.add.AddProductScreen
import com.productdemo.ui.add.AddProductViewModel
import com.productdemo.ui.detail.ProductDetailScreen
import com.productdemo.ui.detail.ProductDetailViewModel
import com.productdemo.ui.listing.ProductListingScreen
import com.productdemo.ui.listing.ProductListingViewModel
import com.productdemo.ui.navigation.Destination
import com.productdemo.ui.theme.ProductDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductDemoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<Destination>(Destination.Listing) }
    val strategy = rememberListDetailSceneStrategy<Destination>()
    
    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
        entryDecorators = listOf(rememberViewModelStoreNavEntryDecorator()),
        sceneStrategies = listOf(strategy),
        entryProvider = { key ->
            when (key) {
                Destination.Listing -> NavEntry(
                    key = key,
                    metadata = mapOf("pane" to ListDetailPaneScaffoldRole.List)
                ) {
                    val viewModel: ProductListingViewModel = viewModel()
                    ProductListingScreen(
                        viewModel = viewModel,
                        onProductClick = { productId ->
                            backStack.add(Destination.Detail(productId))
                        },
                        onAddProductClick = {
                            backStack.add(Destination.AddProduct)
                        }
                    )
                }
                is Destination.Detail -> NavEntry(
                    key = key,
                    metadata = mapOf("pane" to ListDetailPaneScaffoldRole.Detail)
                ) {
                    val viewModel: ProductDetailViewModel = viewModel()
                    ProductDetailScreen(
                        productId = key.productId,
                        viewModel = viewModel,
                        onBackClick = { 
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                        }
                    )
                }
                Destination.AddProduct -> NavEntry(key) {
                    val viewModel: AddProductViewModel = viewModel()
                    AddProductScreen(
                        viewModel = viewModel,
                        onBackClick = { 
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                        }
                    )
                }
            }
        }
    )
}

