package me.dio.soccernews.ui.news

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.dio.soccernews.data.local.SoccerNewsLocalRepository
import me.dio.soccernews.data.remote.SoccerNewsApiRepository
import me.dio.soccernews.domain.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(
    private val newsRepository: SoccerNewsApiRepository,
    private val localNewsRepo: SoccerNewsLocalRepository
) : ViewModel() {
    private val _news = MutableLiveData<State>()
    val news: LiveData<State> = _news

    fun getNews() {
        viewModelScope.launch {
            _news.postValue(State.Loading)
            newsRepository.remoteApi.news.enqueue(object : Callback<List<News>> {
                override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
                    if (response.isSuccessful) {
                        _news.postValue(State.Success(response.body()!!))
                    }
                }

                override fun onFailure(call: Call<List<News>>, t: Throwable) {
                    _news.postValue(State.Error(t))
                }
            })
        }
    }

    fun saveNews(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            localNewsRepo.insert(news)
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val news: List<News>) : State()
        data class Error(val error: Throwable) : State()
    }
}

class NewsViewModelFactory(
    private val remoteNewsRepo: SoccerNewsApiRepository,
    private val localNewsRepo: SoccerNewsLocalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(remoteNewsRepo, localNewsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}