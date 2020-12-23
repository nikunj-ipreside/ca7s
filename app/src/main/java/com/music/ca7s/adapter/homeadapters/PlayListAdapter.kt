package com.music.ca7s.adapter.homeadapters

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.music.ca7s.R
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.model.playlist.Playlist


class PlayListAdapter(var mContext: Context, var list :ArrayList<Playlist>, val songNumber:String, val  dialog: Dialog, val imageView : ImageView )
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            return PlayListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val movie: Playlist = list[position]
            (holder as PlayListViewHolder).bind(movie, mContext)
            (holder as PlayListViewHolder).tv_add!!.tag = list[position]
            (holder as PlayListViewHolder).tv_add!!.id = position
            (holder as PlayListViewHolder).tv_add!!.setOnClickListener(View.OnClickListener {
                val source = it.tag as Playlist
                val id = it.id
                (mContext as HomeActivity).addToPlaylist(imageView,songNumber, source.id.toString())
                dialog.dismiss()
            })

    }

    override fun getItemCount(): Int
    {
        if (!list.isEmpty()) {
            return list.size
        }
        else{
            return 0
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    public fun refreshAdapter(updatedlist: ArrayList<Playlist>)
    {
        list=updatedlist
        notifyDataSetChanged()
    }

}

class PlayListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.row_add_playlist, parent, false)) {

    public var iv_image : ImageView?=null
    public var tv_name: TextView? = null
    public var tv_add: TextView? = null

    init {
        iv_image = itemView.findViewById(R.id.iv_image)
        tv_name = itemView.findViewById(R.id.tv_name)
        tv_add = itemView.findViewById(R.id.tv_add)

    }

    fun bind(countryListData: Playlist, mContext: Context) {
        tv_name?.text=countryListData.name

        if (countryListData.image != null && !countryListData.image.toString().isEmpty()){
            var playlistImage=countryListData.image
            if (playlistImage != null && !playlistImage.isEmpty()) {
                if (playlistImage.contains("/index.php")) {
                    playlistImage = playlistImage.replace("/index.php", "")
                }

                Glide.with(mContext)
                        .load(playlistImage)
                        .placeholder(R.drawable.ic_top_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                        .into(iv_image!!)
            }
//            (mContext as HomeActivity).loadImage(playlistImage,iv_image)
        }
    }
}
