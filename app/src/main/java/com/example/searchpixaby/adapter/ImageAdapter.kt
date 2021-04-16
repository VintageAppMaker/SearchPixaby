package com.example.searchpixaby.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchpixaby.R
import com.example.searchpixaby.model.Hits

class ImageAdapter(var context: Context, var images: List<Hits>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = images[position]
        holder.txtCount.text = "${item.downloads} downloads"

        // large일 경우, 트래픽도 고려해야 함
        Glide.with(context)
            .load(item.largeImageURL)
            .fitCenter()
            .into(holder.imgThumb)


        holder.imgThumb.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.pageURL))
            context.startActivity(browserIntent)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgThumb: ImageView
        var txtCount: TextView

        init {
            imgThumb =
                view.findViewById<View>(R.id.imageView) as ImageView
            txtCount =
                    view.findViewById<View>(R.id.txtCount) as TextView
        }
    }

}