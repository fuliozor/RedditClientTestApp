package com.phonexa.reddit.ui.main

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.phonexa.reddit.R
import com.phonexa.reddit.data.model.Post


class PostsRecyclerViewAdapter(private val onItemClickListener: (item: Post) -> Unit) :
    PagedListAdapter<Post, PostsRecyclerViewAdapter.PostViewHolder>(PostsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: return

        holder.tvTitle.text = post.title
        holder.tvAuthor.text = holder.tvAuthor.context.getString(R.string.lbl_posted_by, post.author)
        holder.tvSubreddit.text = post.subreddit
        holder.tvScore.text = holder.tvScore.context.getString(R.string.lbl_score, formatNumber(post.score))
        holder.tvComments.text = holder.tvScore.context.getString(R.string.lbl_comments, formatNumber(post.comments))

        holder.tvPostDate.text = DateUtils.getRelativeTimeSpanString(
            post.created * 1000, System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        )

        if (!TextUtils.isEmpty(post.thumbnail)) {
            Glide.with(holder.ivPreview).load(post.thumbnail).into(holder.ivPreview)
            holder.ivPreview.visibility = View.VISIBLE
        } else {
            holder.ivPreview.visibility = View.GONE
        }
    }

    inner class PostViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        var tvSubreddit: TextView = itemView.findViewById(R.id.tvSubreddit)
        var tvPostDate: TextView = itemView.findViewById(R.id.tvPostDate)
        var tvScore: TextView = itemView.findViewById(R.id.tvScore)
        var tvComments: TextView = itemView.findViewById(R.id.tvComments)
        var ivPreview: ImageView = itemView.findViewById(R.id.ivPreview)

        init {
            itemView.setOnClickListener {
                val post = getItem(adapterPosition)

                if (post != null) {
                    onItemClickListener.invoke(post)
                }
            }
        }
    }

    companion object {
        private val SUFFIXES: CharArray = charArrayOf('k', 'm', 'g', 't', 'p', 'e')

        val PostsDiffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }

        fun formatNumber(number: Long): String {
            if (number < 1000) {
                // No need to format this
                return number.toString()
            }
            // Convert to a string
            val string = number.toString()
            // The suffix we're using, 1-based
            val magnitude = (string.length - 1) / 3
            // The number of digits we must show before the prefix
            val digits = (string.length - 1) % 3 + 1

            // Build the string
            val value = CharArray(4)
            for (i in 0 until digits) {
                value[i] = string[i]
            }
            var valueLength = digits
            // Can and should we add a decimal point and an additional number?
            if (digits == 1 && string[1] != '0') {
                value[valueLength++] = '.'
                value[valueLength++] = string[1]
            }
            value[valueLength++] = SUFFIXES[magnitude - 1]
            return String(value, 0, valueLength)
        }
    }

}