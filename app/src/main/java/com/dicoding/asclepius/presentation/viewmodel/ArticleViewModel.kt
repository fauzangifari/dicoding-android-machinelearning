package com.dicoding.asclepius.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _articleCancer = MutableLiveData<List<ArticlesItem>>()
    val articleCancer: LiveData<List<ArticlesItem>> get() = _articleCancer

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getArticleCancer() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                Log.d("ArticleViewModel", "Fetching articles...")
                val response = articleRepository.getArticleCancer()
                Log.d("ArticleViewModel", "Response: $response")
                _articleCancer.value = response.articles?.filterNotNull() ?: emptyList()
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e("ArticleViewModel", "getArticleCancer: ${e.message}")
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
