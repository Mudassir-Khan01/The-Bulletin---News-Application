package com.example.thebulletin.ui.fragments

import android.content.ClipData.Item
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thebulletin.R
import com.example.thebulletin.adapters.NewsAdapter
import com.example.thebulletin.ui.MainActivity
import com.example.thebulletin.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment:Fragment(R.layout.fragment_saved_news) {
    lateinit var rvSavedNews:RecyclerView
    lateinit var newsAdapter:NewsAdapter
    lateinit var viewModel:NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment3,
                bundle
            )
        }

        //implementing onswipe function for deleting article
        val itemTouchHelperCallBack=object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val article=newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Successfully deleted article",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rvSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles-> //observing and changing as soon as possible
            newsAdapter.differ.submitList(articles)
        })
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvSavedNews= view?.findViewById<RecyclerView>(R.id.rvSavedNews)!!
        rvSavedNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}
