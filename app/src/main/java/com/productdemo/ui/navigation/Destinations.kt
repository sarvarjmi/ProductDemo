package com.productdemo.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination : NavKey {
    @Serializable
    data object Listing : Destination
    
    @Serializable
    data class Detail(val productId: Int) : Destination
    
    @Serializable
    data object AddProduct : Destination
}
