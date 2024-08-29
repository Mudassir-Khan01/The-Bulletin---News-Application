package com.example.thebulletin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thebulletin.R
import com.example.thebulletin.models.Article
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    private val differCallBack=object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            val ivArticleImage = findViewById<ImageView>(R.id.ivArticleImage)
            Glide.with(this).load(article.urlToImage).error(R.drawable.breakingnews).into(ivArticleImage)

            val tvSource = findViewById<TextView>(R.id.tvSource)
            tvSource.text = article.source?.name ?: "Unknown Source"

            val tvTitle = findViewById<TextView>(R.id.tvTitle)
            tvTitle.text = article.title ?: "No Title"

            val tvDescription = findViewById<TextView>(R.id.tvDescription)
            tvDescription.text = article.author?: "No Description Available"

            val tvPublishedAt = findViewById<TextView>(R.id.tvPublishedAt)
            tvPublishedAt.text = article.publishedAt ?: "Unknown Date"

            setOnClickListener{
                onItemClickListener?.let { it(article) }
            }
        }
    }
    fun addItem(article: Article, position: Int) {
        val curList = differ.currentList.toMutableList()
        curList.add(position, article)
        differ.submitList(curList)
    }
    fun deleteArticle(article: Article) {
        val curList = differ.currentList.toMutableList()
        curList.remove(article)
        differ.submitList(curList)
    }

    private var onItemClickListener:((Article)->Unit)?=null

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }

}