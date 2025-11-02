package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.model.Product
import com.cineplusapp.cineplusspaapp.data.model.ProductType
import com.cineplusapp.cineplusspaapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {

    private val filter = MutableStateFlow<ProductType?>(null)

    val products: StateFlow<List<Product>> =
        filter.flatMapLatest { repo.list(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(type: ProductType?) { filter.value = type }

    fun seed() = viewModelScope.launch { repo.seedIfEmpty() }

    fun product(id: Int): StateFlow<Product?> =
        repo.byId(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
