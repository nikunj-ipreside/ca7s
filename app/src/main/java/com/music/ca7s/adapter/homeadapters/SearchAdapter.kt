package com.music.ca7s.adapter.homeadapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.music.ca7s.R
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.contant.RecylerViewItemClickListener
import com.music.ca7s.contant.RecylerViewSongItemClickListener
import com.music.ca7s.fragment.BaseFragment
import com.music.ca7s.listener.RecyclerItemSearchedSongListenerListener
import com.music.ca7s.mediaplayer.Song
import com.music.ca7s.model.SearchData
import com.music.ca7s.model.genre_list.GenreDatum
import com.music.ca7s.model.genre_list.GenrePojo
import com.music.ca7s.model.topcas.TopCasData

class SearchAdapter(private var list: ArrayList<SearchData>, public var mContext: Context, var listener : RecyclerItemSearchedSongListenerListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            return SearchViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val movie: SearchData = list[position]
            (holder as SearchViewHolder).bind(movie, mContext)
            (holder as SearchViewHolder).tv_data_name!!.tag = list[position]
            (holder as SearchViewHolder).tv_data_name!!.id = position

        (holder as SearchViewHolder).iv_download!!.tag = list[position]
        (holder as SearchViewHolder).iv_download!!.id = position

            (holder as SearchViewHolder).tv_data_name!!.setOnClickListener(View.OnClickListener {
                val source = it.tag as SearchData
                val id = it.id
                    listener.onClickedonSong(id, source)
            })


        (holder as SearchViewHolder).iv_download!!.setOnClickListener(View.OnClickListener {
            val source = it.tag as SearchData
            val id = it.id
            listener.onDownloadClick(id, source)
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

    public fun refreshAdapter(updatedlist: ArrayList<SearchData>)
    {
        list=updatedlist
        notifyDataSetChanged()
    }

}

class SearchViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.row_search, parent, false)) {

    public var tv_data_name: TextView? = null
    public var iv_download: ImageView? = null
    init {
        tv_data_name = itemView.findViewById(R.id.tv_name)
        iv_download = itemView.findViewById(R.id.iv_download)
    }

    fun bind(countryListData: SearchData, mContext: Context) {
        tv_data_name?.text=countryListData.songTitle

    }
}
