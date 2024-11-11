package com.dicoding.asclepius.util

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.data.remote.response.ArticlesItem

class ArticleDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is ArticlesItem && newItem is ArticlesItem -> oldItem.title == newItem.title
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is ArticlesItem && newItem is ArticlesItem -> oldItem.title == newItem.title
            else -> false
        }
    }
}