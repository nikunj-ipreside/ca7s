package com.music.ca7s.adapter.homeadapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.music.ca7s.R
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.utils.AppConstants
import com.music.ca7s.contant.RecylerViewSongItemClickListener
import com.music.ca7s.mediaplayer.Song

class DownloadedSongsAdapter(var list: List<Song>,var fiteredList: List<Song>, public var mContext: Context,var listener : RecylerViewSongItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            return DownloadedSongsViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val movie: Song = fiteredList[position]
            (holder as DownloadedSongsViewHolder).bind(movie, mContext)
            (holder as DownloadedSongsViewHolder).itemView!!.tag = list[position]
            (holder as DownloadedSongsViewHolder).itemView!!.id = position

        (holder as DownloadedSongsViewHolder).iv_dot!!.tag = list[position]
        (holder as DownloadedSongsViewHolder).iv_dot!!.id = position
            (holder as DownloadedSongsViewHolder).itemView!!.setOnClickListener(View.OnClickListener {
                val source = it.tag as Song
                val id = it.id
                    listener.onSongSelected(id, source,AppConstants.DOWNLOADED_PLAYLIST)
            })


        (holder as DownloadedSongsViewHolder).iv_dot!!.setOnClickListener(View.OnClickListener {
            val source = it.tag as Song
            val id = it.id
            listener.onDotSelected(id, source)
        })

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    fiteredList  = list
                } else {
                    val filteredListSong = ArrayList<Song>()
                    for (row in list) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.songTitle.toLowerCase().contains(charString.toLowerCase()) || row.songArtist.contains(charSequence)) {
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
                fiteredList = filterResults.values as ArrayList<Song>
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int
    {
        if (!fiteredList.isEmpty()) {
            return fiteredList.size
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

    public fun refreshAdapter(updatedlist: List<Song>)
    {
        fiteredList = updatedlist
        list=updatedlist
        notifyDataSetChanged()
    }

}

class DownloadedSongsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.row_downloaded, parent, false)) {

    public var tv_data_name: TextView? = null
    public var iv_image: ImageView? = null
    public var iv_dot: ImageView? = null
    private var txtSongContent : TextView?=null
    init {
        tv_data_name = itemView.findViewById(R.id.tv_song_name)
        iv_image = itemView.findViewById(R.id.iv_song_image)
        iv_dot = itemView.findViewById(R.id.imgDot)
        txtSongContent = itemView.findViewById(R.id.txtSongContent)
    }

    fun bind(countryListData: Song, mContext: Context) {
        tv_data_name?.text=countryListData.songTitle
        txtSongContent?.text=countryListData.songArtist
        if (countryListData.from.toString().equals(AppConstants.FROM_MY_MUSIC)){
            if (countryListData.songImagePath != null && !countryListData.songImagePath.isEmpty()) {
                (mContext as HomeActivity).loadImage(countryListData.songImagePath, iv_image)
            }
        }else {
            if (countryListData.songImageUrl != null && !countryListData.songImageUrl.isEmpty()) {
                (mContext as HomeActivity).loadImage(countryListData.songImageUrl, iv_image)
            }
        }
    }
}
