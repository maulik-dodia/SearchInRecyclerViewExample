package com.searchinrecyclerviewexample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.searchinrecyclerviewexample.R
import com.searchinrecyclerviewexample.model.SearchImageRes
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class SearchImageAdapter :
    ListAdapter<SearchImageRes, SearchImageAdapter.ViewHolder>(TaskDiffCallBack()) {

    class TaskDiffCallBack : DiffUtil.ItemCallback<SearchImageRes>() {
        override fun areItemsTheSame(oldItem: SearchImageRes, newItem: SearchImageRes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchImageRes, newItem: SearchImageRes): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val singleSearchImageObj = getItem(position)

        singleSearchImageObj?.let { searchImage ->
            holder.bind(searchImage)
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val pbPreviewPic: ProgressBar = itemView.findViewById(R.id.pb_preview_pic)
        private val ivPreviewPic: AppCompatImageView = itemView.findViewById(R.id.iv_preview_pic)
        private val tvViews: AppCompatTextView = itemView.findViewById(R.id.tv_views)
        fun bind(searchImage: SearchImageRes) {
            Picasso.get()
                .load(searchImage.previewURL)
                .into(ivPreviewPic, object : Callback {
                    override fun onSuccess() {
                        pbPreviewPic.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        pbPreviewPic.visibility = View.GONE
                    }
                })
            tvViews.text = String.format(
                tvViews.context.getString(R.string.str_x_of_views), searchImage.views
            )
        }
    }
}