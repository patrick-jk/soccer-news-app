package me.dio.soccernews.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.dio.soccernews.domain.News

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(news: News)

    @Query("SELECT * FROM news WHERE favorite = 1")
    fun getFavoriteNews(): LiveData<List<News>>
}