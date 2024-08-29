package com.example.thebulletin.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.thebulletin.R
import com.example.thebulletin.ui.MainActivity
import com.example.thebulletin.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ArticleFragment:Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
     lateinit var webView:WebView
     lateinit var fab:FloatingActionButton
    val args:ArticleFragmentArgs by navArgs()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        val article = args.article
        Log.d("ArticleFragment", "URL: ${article.url}")
        webView = view.findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled=true
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url ?: "https://www.google.com") // Default URL in case of null
        }

        fab=view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article Saved Successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}