package com.productdemo.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productdemo.domain.usecase.GetProductDetailUseCase
import com.productdemo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProductDetailEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.LoadProduct -> loadProduct(intent.id)
        }
    }

    private fun loadProduct(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val resource = getProductDetailUseCase(id)
            when (resource) {
                is Resource.Success -> {
                    _state.update { it.copy(product = resource.data, isLoading = false) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = resource.message) }
                    _effect.emit(ProductDetailEffect.ShowError(resource.message))
                }
                Resource.Loading -> {
                    // Handled before the call
                }
            }
        }
    }
}
