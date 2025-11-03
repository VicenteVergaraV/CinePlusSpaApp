// ProductDetailViewModel.kt
package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.model.Product
import com.cineplusapp.cineplusspaapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {
    fun product(id: Int): StateFlow<Product?> =
        repo.byId(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
