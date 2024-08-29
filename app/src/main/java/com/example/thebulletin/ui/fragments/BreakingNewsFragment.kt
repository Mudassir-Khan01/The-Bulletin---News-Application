package com.example.thebulletin.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thebulletin.R
import com.example.thebulletin.adapters.NewsAdapter
import com.example.thebulletin.models.Constants.Companion.QUERY_PAGE_SIZE
import com.example.thebulletin.ui.MainActivity
import com.example.thebulletin.ui.NewsViewModel
import com.example.thebulletin.util.Resource

class BreakingNewsFragment:Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    lateinit var rvBreakingNews:RecyclerView
    lateinit var paginationProgressBar:ProgressBar
    val TAG="Breaking News"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let {newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages=newsResponse.totalResults/ QUERY_PAGE_SIZE+2
                        isLastPage=viewModel.breakingNewsPage==totalPages
                        if(isLastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let { message->
                        Log.e(TAG,"An Error Occured: $message" )
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }
    private fun hideProgressBar(){
        paginationProgressBar= view?.findViewById<ProgressBar>(R.id.paginationProgressBar)!!
        paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar(){
        paginationProgressBar= view?.findViewById<ProgressBar>(R.id.paginationProgressBar)!!
        paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    val scrollListener=object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPage=!isLoading && !isLastPage
            val isAtLastItem =firstVisibleItemPosition+visibleItemCount >= totalItemCount
            val isNotAtBeginning =firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible=totalItemCount>=QUERY_PAGE_SIZE
            val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling=false
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvBreakingNews= view?.findViewById<RecyclerView>(R.id.rvBreakingNews)!!
        rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}