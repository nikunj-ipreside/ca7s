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
import com.music.ca7s.model.OfflineSearchModel
import com.music.ca7s.model.genre_list.GenreDatum
import com.music.ca7s.model.genre_list.GenrePojo
import com.music.ca7s.model.topcas.TopCasData

class SearchHistoryAdapter(private var list: List<OfflineSearchModel>, public var mContext: Context, var listener : RecyclerItemSearchedSongListenerListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            return SearchHistoryViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val movie: OfflineSearchModel = list[position]
            (holder as SearchHistoryViewHolder).bind(movie, mContext)
            (holder as SearchHistoryViewHolder).itemView!!.tag = list[position]
            (holder as SearchHistoryViewHolder).itemView!!.id = position
            (holder as SearchHistoryViewHolder).itemView!!.setOnClickListener(View.OnClickListener {
                val source = it.tag as OfflineSearchModel
                val id = it.id
                    listener.onClickedOnHistoryItem(id, source)
            })

        (holder as SearchHistoryViewHolder).tv_remove!!.tag = list[position]
        (holder as SearchHistoryViewHolder).tv_remove!!.id = position
        (holder as SearchHistoryViewHolder).tv_remove!!.setOnClickListener(View.OnClickListener {
            val source = it.tag as OfflineSearchModel
            val id = it.id
            listener.onRemove(id, source)
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

    public fun refreshAdapter(updatedlist: List<OfflineSearchModel>)
    {
        list=updatedlist
        notifyDataSetChanged()
    }

}

class SearchHistoryViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.row_search_history, parent, false)) {

    public var tv_data_name: TextView? = null
    public var tv_remove: TextView? = null

    init {
        tv_data_name = itemView.findViewById(R.id.tv_name)
        tv_remove = itemView.findViewById(R.id.tv_remove)

    }

    fun bind(countryListData: OfflineSearchModel, mContext: Context) {
        tv_data_name?.text=countryListData.name
    }
}
