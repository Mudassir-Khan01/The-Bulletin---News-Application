package com.example.thebulletin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebulletin.models.Article
import com.example.thebulletin.models.NewsResponse
import com.example.thebulletin.repository.NewsRepository
import com.example.thebulletin.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Locale.IsoCountryCode

class NewsViewModel(// we have an instance of news repository
    val newsRepository: NewsRepository
) :ViewModel(){
    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsResponse: NewsResponse?=null

    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse: NewsResponse?=null

    init {
        getBreakingNews("us")

    }
//breaking news
    fun getBreakingNews(countryCode: String)=viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)// all magic happens, spent fn
        breakingNews.postValue(handleBreakingNewsResponse(response))

    }
    //search news
    fun searchNews(searchQuery:String)=viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response=newsRepository.searchNews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }else{
                    val oldArticle=breakingNewsResponse?.articles
                    val newArticle=resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                searchNewsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                }else{
                    val oldArticle=searchNewsResponse?.articles
                    val newArticle=resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews()=newsRepository.getSavedNews()  //not using coroutine

    fun deleteArticle(article: Article)=viewModelScope.launch { //using coroutine
        newsRepository.deleteArticle(article)
    }
}