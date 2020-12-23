package com.music.ca7s.adapter.homeadapters

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.music.ca7s.R
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.utils.AppConstants
import com.music.ca7s.contant.RecylerViewItemClickListener
import com.music.ca7s.listener.onPositionSelected
import com.music.ca7s.mediaplayer.Song
import com.music.ca7s.model.DownloadPlaylistModel
import com.music.ca7s.model.playlist.Playlist
import com.music.ca7s.utils.SwipeRevealLayout


class DowloadedPlayListAdapterMain(var mContext: Context, var list: ArrayList<DownloadPlaylistModel>, var fiteredList: ArrayList<DownloadPlaylistModel>, val listener: onPositionSelected)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>()  , Filterable  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DOwnloadedMainViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val movie: DownloadPlaylistModel = fiteredList[position]
        (holder as DOwnloadedMainViewHolder).bind(movie, mContext)
        (holder as DOwnloadedMainViewHolder).ll_main!!.tag = list[position]
        (holder as DOwnloadedMainViewHolder).ll_main!!.id = position
        (holder as DOwnloadedMainViewHolder).delete_button!!.tag = list[position]
        (holder as DOwnloadedMainViewHolder).delete_button!!.id = position
        (holder as DOwnloadedMainViewHolder).edit_button!!.tag = list[position]
        (holder as DOwnloadedMainViewHolder).edit_button!!.id = position
        (holder as DOwnloadedMainViewHolder).ll_main!!.setOnClickListener(View.OnClickListener {
//            val source = it.tag as Playlist
            val id = it.id
            listener.onItemSelected(id)
        })

        (holder as DOwnloadedMainViewHolder).edit_button!!.setOnClickListener(View.OnClickListener {
//            val source = it.tag as Playlist
            val id = it.id
            listener.onMenuSelected(AppConstants.UPDATE, id)
            (holder as DOwnloadedMainViewHolder).swipe_layout!!.close(true)
        })

        (holder as DOwnloadedMainViewHolder).delete_button!!.setOnClickListener(View.OnClickListener {
//            val source = it.tag as Playlist
            val id = it.id
            listener.onMenuSelected(AppConstants.DELETE, id)
            (holder as DOwnloadedMainViewHolder).swipe_layout!!.close(true)
        })

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    fiteredList  = list
                } else {
                    val filteredListSong = ArrayList<DownloadPlaylistModel>()
                    for (row in list) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name.toLowerCase().contains(charString.toLowerCase()) || row.name.contains(charSequence)) {
                            filteredListSong.add(row)
                        }
                    }

                    fiteredList = filteredListSong
                }

                val filterResults = FilterResults()
                filterResults.values = fiteredList
                return filterResults
            }

            protected override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                fiteredList = filterResults.values as ArrayList<DownloadPlaylistModel>
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int {
        if (!fiteredList.isEmpty()) {
            return fiteredList.size
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

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    public fun refreshAdapter(updatedlist: ArrayList<DownloadPlaylistModel>) {
        fiteredList =  updatedlist
        list = updatedlist
        notifyDataSetChanged()
    }

}

class DOwnloadedMainViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.row_add_playlist_main, parent, false)) {
    public var iv_image: ImageView? = null
    public var tv_name: TextView? = null
    public var tv_add: TextView? = null
    public var edit_button: ImageView? = null
    public var delete_button: ImageView? = null
    public var ll_main:LinearLayout? =null
    public var swipe_layout:SwipeRevealLayout?=null

    init {
        iv_image = itemView.findViewById(R.id.iv_image)
        tv_name = itemView.findViewById(R.id.tv_name)
        tv_add = itemView.findViewById(R.id.tv_add)
        edit_button = itemView.findViewById(R.id.edit_button)
        delete_button = itemView.findViewById(R.id.delete_button)
        ll_main = itemView.findViewById(R.id.ll_main)
        swipe_layout = itemView.findViewById(R.id.swipe_layout)

    }

    fun bind(countryListData: DownloadPlaylistModel, mContext: Context) {
        tv_name?.text = countryListData.name
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
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                        .into(iv_image!!)
            }
            else {
                Glide.with(mContext)
                        .load(R.drawable.ic_top_placeholder)
                        .placeholder(R.drawable.ic_top_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                        .into(iv_image!!)
            }

//            (mContext as HomeActivity).loadImage(playlistImage,iv_image)
        }
    }
}
