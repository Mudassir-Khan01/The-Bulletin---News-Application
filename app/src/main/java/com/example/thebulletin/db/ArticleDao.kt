package com.example.thebulletin.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.thebulletin.models.Article


@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //determine what happens when that article already exist in out database
    suspend fun upsert(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article?>> // live changes

    @Delete
    suspend fun deleteArticle(article: Article)
}