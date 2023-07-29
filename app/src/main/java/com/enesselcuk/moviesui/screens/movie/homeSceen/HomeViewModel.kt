package com.enesselcuk.moviesui.screens.movie.homeSceen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesselcuk.moviesui.repos.reposRemote.Repos
import com.enesselcuk.moviesui.source.model.response.MoviesResponse
import com.enesselcuk.moviesui.source.model.response.TrendingResponse
import com.enesselcuk.moviesui.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repos: Repos) : ViewModel() {

    private val _getMoviesFlow = MutableStateFlow<MoviesResponse?>(null)
    val getMoviesFlow = _getMoviesFlow.asStateFlow()

    private val _getUpComingFlow = MutableStateFlow<MoviesResponse?>(null)
    val getUpComingFlow = _getUpComingFlow.asStateFlow()

    private val _getToastMessage: MutableLiveData<String?> = MutableLiveData()
    val getToast: MutableLiveData<String?> = _getToastMessage

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    var isVisible = mutableStateOf(false)
        private set

    fun getMovies(title: String, language: String, page: Int) {
        viewModelScope.launch {
            repos.getMovies(title, language, page).collectLatest { moviesResponse ->
                when (moviesResponse) {
                    is NetworkResult.Loading -> {
                        _loading.value = true
                    }
                    is NetworkResult.Success -> {
                        _getMoviesFlow.emit(moviesResponse.data)
                        _loading.value = false
                        isVisible.value = true
                    }
                    is NetworkResult.Error -> {
                        _getToastMessage.value = moviesResponse.message
                        isVisible.value = false
                    }
                }
            }
        }
    }

    fun getMoviesUpComing(title: String, language: String, page: Int) {
        viewModelScope.launch {
            repos.getMovies(title, language, page).collectLatest { moviesResponse ->
                when (moviesResponse) {
                    is NetworkResult.Loading -> {
                        _loading.value = true
                    }
                    is NetworkResult.Success -> {
                        _getUpComingFlow.emit(moviesResponse.data)
                        _loading.value = false
                        isVisible.value = true
                    }
                    is NetworkResult.Error -> {
                        _getToastMessage.value = moviesResponse.message
                        isVisible.value = false
                    }
                }
            }
        }
    }

    private val _getMoviesTrendingFlow = MutableStateFlow<TrendingResponse?>(null)
    val getMoviesTrendingFlow = _getMoviesTrendingFlow.asStateFlow()
    fun getMoviesTrending() {
        viewModelScope.launch {
            repos.getMoviesTrending().collectLatest { moviesResponse ->
                when (moviesResponse) {
                    is NetworkResult.Loading -> {
                        _loading.value = true
                    }
                    is NetworkResult.Success -> {
                        _getMoviesTrendingFlow.emit(moviesResponse.data)
                        _loading.value = false
                        isVisible.value = true
                    }
                    is NetworkResult.Error -> {
                        _getToastMessage.value = moviesResponse.message
                        isVisible.value = false
                    }
                }
            }
        }
    }
}