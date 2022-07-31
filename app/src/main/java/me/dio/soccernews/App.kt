package me.dio.soccernews

import android.app.Application
import me.dio.soccernews.data.local.SoccerNewsLocalRepository
import me.dio.soccernews.data.local.AppDatabase

class App : Application() {
    private val database by lazy { AppDatabase.getInstance(this) }
    val localRepository by lazy { SoccerNewsLocalRepository(database.newsDao()) }
}