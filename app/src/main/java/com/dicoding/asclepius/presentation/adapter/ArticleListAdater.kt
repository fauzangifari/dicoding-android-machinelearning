package com.dicoding.asclepius.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemListArticleBinding
import com.dicoding.asclepius.presentation.view.DetailActivity
import com.dicoding.asclepius.util.ArticleDiffCallback

class ArticleListAdapter : ListAdapter<Any, RecyclerView.ViewHolder>(ArticleDiffCallback()) {

    class ApiViewHolder(private val binding: ItemListArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(articleImage)

                articleTitle.text = article.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiViewHolder {
        val binding = ItemListArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val article = getItem(position) as ArticlesItem
        (holder as ApiViewHolder).bind(article)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("article", article)
            }
            context.startActivity(intent)
        }
    }
}
