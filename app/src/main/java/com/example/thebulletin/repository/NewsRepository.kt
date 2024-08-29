package com.example.thebulletin.repository

import com.example.thebulletin.api.RetrofitInstance
import com.example.thebulletin.db.ArticleDatabase
import com.example.thebulletin.models.Article
import java.util.Locale.IsoCountryCode

class NewsRepository(val db:ArticleDatabase)// to get data from our database and remote data source ie from retrofit or api
{
    suspend fun getBreakingNews(countryCode: String,pageNumber: Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery:String, pageNumber:Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article:Article)=db.getarticleDao().upsert(article)

    fun  getSavedNews()=db.getarticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getarticleDao().deleteArticle(article)
}