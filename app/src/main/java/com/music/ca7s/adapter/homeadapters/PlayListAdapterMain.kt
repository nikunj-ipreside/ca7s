package com.music.ca7s.adapter.homeadapters

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.music.ca7s.R
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.utils.AppConstants
import com.music.ca7s.contant.RecylerViewItemClickListener
import com.music.ca7s.listener.onPositionSelected
import com.music.ca7s.model.playlist.Playlist
import com.music.ca7s.utils.SwipeRevealLayout


class PlayListAdapterMain(var mContext: Context, var list: ArrayList<Playlist>, val listener: onPositionSelected)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlayListMainViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val movie: Playlist = list[position]
        (holder as PlayListMainViewHolder).bind(movie, mContext)
        (holder as PlayListMainViewHolder).ll_main!!.tag = list[position]
        (holder as PlayListMainViewHolder).ll_main!!.id = position
        (holder as PlayListMainViewHolder).delete_button!!.tag = list[position]
        (holder as PlayListMainViewHolder).delete_button!!.id = position
        (holder as PlayListMainViewHolder).edit_button!!.tag = list[position]
        (holder as PlayListMainViewHolder).edit_button!!.id = position
        (holder as PlayListMainViewHolder).ll_main!!.setOnClickListener(View.OnClickListener {
            val source = it.tag as Playlist
            val id = it.id
            listener.onItemSelected(id)
        })

        (holder as PlayListMainViewHolder).edit_button!!.setOnClickListener(View.OnClickListener {
            val source = it.tag as Playlist
            val id = it.id
            listener.onMenuSelected(AppConstants.UPDATE, id)
            (holder as PlayListMainViewHolder).swipe_layout!!.close(true)
        })

        (holder as PlayListMainViewHolder).delete_button!!.setOnClickListener(View.OnClickListener {
            val source = it.tag as Playlist
            val id = it.id
            listener.onMenuSelected(AppConstants.DELETE, id)
            (holder as PlayListMainViewHolder).swipe_layout!!.close(true)
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

    public fun refreshAdapter(updatedlist: ArrayList<Playlist>) {
        list = updatedlist
        notifyDataSetChanged()
    }

}

class PlayListMainViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
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

    fun bind(countryListData: Playlist, mContext: Context) {
        tv_name?.text = countryListData.name
        if (countryListData.image != null && !countryListData.image.toString().isEmpty()){
            var playlistImage=countryListData.image
            if (playlistImage.contains("/index.php")){
                playlistImage = playlistImage.replace("/index.php","")
            }

            (mContext as HomeActivity).loadImage(playlistImage,iv_image)
        }
    }
}
