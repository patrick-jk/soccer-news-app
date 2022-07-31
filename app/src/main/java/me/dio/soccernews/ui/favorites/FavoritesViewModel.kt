package me.dio.soccernews.ui.favorites

import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.dio.soccernews.data.local.SoccerNewsLocalRepository
import me.dio.soccernews.domain.News
class FavoritesViewModel(private val newsRepository: SoccerNewsLocalRepository) : ViewModel() {

    val favoriteNews: LiveData<List<News>> = newsRepository.favoriteNews

    fun save(news: News) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.insert(news)
    }
}

class FavoritesViewModelFactory(private val newsRepository: SoccerNewsLocalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}