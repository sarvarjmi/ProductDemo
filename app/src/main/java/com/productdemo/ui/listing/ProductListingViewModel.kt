package com.productdemo.ui.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productdemo.domain.usecase.GetProductsUseCase
import com.productdemo.domain.usecase.SearchProductsUseCase
import com.productdemo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListingViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListingState())
    val state: StateFlow<ProductListingState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProductListingEffect>()
    val effect = _effect.asSharedFlow()

    private var observationJob: Job? = null
    private var searchJob: Job? = null
    private val PAGE_SIZE = 10

    init {
        observeProducts()
        loadNextPage()
    }

    fun onIntent(intent: ProductListingIntent) {
        when (intent) {
            ProductListingIntent.LoadNextPage -> loadNextPage()
            ProductListingIntent.Refresh -> refresh()
            is ProductListingIntent.Search -> {
                _state.update { it.copy(searchQuery = intent.query, skip = 0, isEndReached = false) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    observeProducts()
                    loadNextPage()
                }
            }
        }
    }

    private fun observeProducts() {
        observationJob?.cancel()
        observationJob = viewModelScope.launch {
            val query = _state.value.searchQuery
            val flow = if (query.isEmpty()) {
                getProductsUseCase()
            } else {
                searchProductsUseCase(query)
            }

            flow.collectLatest { products ->
                _state.update { it.copy(products = products) }
            }
        }
    }

    private fun loadNextPage() {
        if (_state.value.isLoading || _state.value.isEndReached) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val query = _state.value.searchQuery
            val skip = _state.value.skip
            
            val resource = if (query.isEmpty()) {
                getProductsUseCase.refresh(PAGE_SIZE, skip)
            } else {
                searchProductsUseCase.fetch(query, PAGE_SIZE, skip)
            }

            when (resource) {
                is Resource.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            skip = skip + PAGE_SIZE,
                            isEndReached = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = resource.message) }
                    _effect.emit(ProductListingEffect.ShowError(resource.message))
                }
                Resource.Loading -> {
                    // Handled before the call
                }
            }
        }
    }

    private fun refresh() {
        _state.update { it.copy(skip = 0, isEndReached = false) }
        loadNextPage()
    }
}
