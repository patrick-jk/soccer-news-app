package me.dio.soccernews.data.local

import me.dio.soccernews.domain.News

class SoccerNewsLocalRepository(private val newsDao: NewsDao) {

    val favoriteNews = newsDao.getFavoriteNews()

    suspend fun insert(news: News) = newsDao.save(news)
}