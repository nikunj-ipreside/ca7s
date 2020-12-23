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
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.music.ca7s.R
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.localdatabase.DatabaseHandler
import com.music.ca7s.mediaplayer.Song
import com.music.ca7s.model.DownloadPlaylistModel
import com.music.ca7s.model.playlist.Playlist
import java.io.StringReader
import java.lang.reflect.Type


class DownloadPlayListAdapter(var mContext: Context, var list :ArrayList<DownloadPlaylistModel>,
                              val  dialog: Dialog,val song:Song, val imageView : ImageView,val databaseHandler: DatabaseHandler )
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            return DownloadPlayListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val movie: DownloadPlaylistModel = list[position]
            (holder as DownloadPlayListViewHolder).bind(movie, mContext)
            (holder as DownloadPlayListViewHolder).tv_add!!.tag = list[position]
            (holder as DownloadPlayListViewHolder).tv_add!!.id = position
            (holder as DownloadPlayListViewHolder).tv_add!!.setOnClickListener(View.OnClickListener {
                val source = it.tag as DownloadPlaylistModel
                val id = it.id
                song.setPlaylist(true)
                val gson = Gson()
//                var stringReader: StringReader = StringReader(list.get(id).value)
                val myLIst = gson.fromJson(list.get(id).value,Array<Song>::class.java).toList()
                val myNewList = ArrayList<Song>()
                myNewList.addAll(myLIst)

                var isAlreadyInPlayLIst = false
                for (i in 0 until myNewList.size) {
                    if (myNewList.get(i).songID.toString().equals(song.songID)){
                        isAlreadyInPlayLIst = true;
                        break
                    }
                }

                if (isAlreadyInPlayLIst) {
                    Toast.makeText(mContext, mContext.getString(R.string.already_in_playlist),Toast.LENGTH_SHORT).show()
                } else {
                    myNewList.add(song)
                    val values = gson.toJson(myNewList)
                    val playlistModel= list.get(id)
                    playlistModel.value = values
                    databaseHandler.updatePlaylist(playlistModel)
                    imageView.setImageResource(R.drawable.ic_playlist_add_theme)
                }
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

    public fun refreshAdapter(updatedlist: ArrayList<DownloadPlaylistModel>)
    {
        list=updatedlist
        notifyDataSetChanged()
    }

}


class DownloadPlayListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.row_add_playlist, parent, false)) {

    public var iv_image : ImageView?=null
    public var tv_name: TextView? = null
    public var tv_add: TextView? = null

    init {
        iv_image = itemView.findViewById(R.id.iv_image)
        tv_name = itemView.findViewById(R.id.tv_name)
        tv_add = itemView.findViewById(R.id.tv_add)

    }

    fun bind(countryListData: DownloadPlaylistModel, mContext: Context) {
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
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                        .into(iv_image!!)
//                (mContext as HomeActivity).loadImage(playlistImage, iv_image)
            }
            else {
                Glide.with(mContext)
                        .load(R.drawable.ic_top_placeholder)
                        .placeholder(R.drawable.ic_top_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                        .into(iv_image!!)
            }
        }
    }
}
