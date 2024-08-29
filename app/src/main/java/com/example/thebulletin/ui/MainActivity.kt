package com.example.thebulletin.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.thebulletin.R
import com.example.thebulletin.databinding.ActivityMainBinding
import com.example.thebulletin.db.ArticleDatabase
import com.example.thebulletin.repository.NewsRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: NewsViewModel
//    lateinit var bottomNav:BottomNavigationView
//    lateinit var newsNavHost:FrameLayout
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navHostFrag=supportFragmentManager
            .findFragmentById(R.id.fragment_container_host) as NavHostFragment
        val navController=navHostFrag.navController


    val newsRepository=NewsRepository(ArticleDatabase(this))
    val viewModelProviderFactory=NewsViewModelProviderFactory(newsRepository)
    viewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

    binding.bottomNavigationView.setupWithNavController(navController)


}
}