package com.music.ca7s.adapter.homeadapters

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
import com.music.ca7s.contant.FavouriteRecylerViewItemClickListener
import com.music.ca7s.contant.RecylerViewItemClickListener
import com.music.ca7s.fragment.BaseFragment
import com.music.ca7s.model.favourite.album.AlbumData
import com.music.ca7s.model.genre_list.GenreDatum
import com.music.ca7s.model.genre_list.GenrePojo
import com.music.ca7s.model.topcas.TopCasData

class FavouriteCollectionAdapter(private var list: List<AlbumData>, public var mContext: Context, var listener: FavouriteRecylerViewItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavouriteCollectionViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val movie: AlbumData = list[position]
        (holder as FavouriteCollectionViewHolder).bind(movie, mContext)
        (holder as FavouriteCollectionViewHolder).itemView!!.tag = list[position]
        (holder as FavouriteCollectionViewHolder).itemView!!.id = position
        (holder as FavouriteCollectionViewHolder).itemView!!.setOnClickListener(View.OnClickListener {
            val source = it.tag as AlbumData
            val id = it.id
            listener.onCollectionClicked(id, source)
        })

    }

    override fun getItemCount(): Int {
        if (!list.isEmpty()) {
            return list.size
        } else {
            return 0
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    public fun refreshAdapter(updatedlist: List<AlbumData>) {
        list = updatedlist
        notifyDataSetChanged()
    }

}

class FavouriteCollectionViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.item_browse_all, parent, false)) {

    public var tv_data_name: TextView? = null
    public var iv_image: ImageView? = null

    init {
        tv_data_name = itemView.findViewById(R.id.tv_name)
        iv_image = itemView.findViewById(R.id.iv_image)
    }

    fun bind(countryListData: AlbumData, mContext: Context) {
        tv_data_name?.text = countryListData.type

        if (countryListData.imageIcon != null && !countryListData.imageIcon.isEmpty()) {
            if (iv_image != null) {
                var playlistImage: String? = countryListData.imageIcon
                if (playlistImage!!.contains("/index.php")) {
                    playlistImage = playlistImage.replace("/index.php", "")
                }
                if (playlistImage != null && !playlistImage.isEmpty()) {
                    Glide.with(mContext)
                            .load(playlistImage)
                            .placeholder(R.drawable.ic_top_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                            .into(iv_image!!)
                }
            }
        }
    }
}
