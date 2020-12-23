package com.music.ca7s.adapter.homeadapters

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.music.ca7s.AppLevelClass.iv_static_star_LIke
import com.music.ca7s.R
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.utils.AppConstants
import com.music.ca7s.listener.RecyclerItemSongListenerListener
import com.music.ca7s.mediaplayer.Song
import kotlinx.android.synthetic.main.row_playlist1.view.*
import java.util.*
import kotlin.collections.ArrayList


class PlayListSongAdapter(var list: java.util.ArrayList<Song>,var fiteredList: java.util.ArrayList<Song>, var mContext: Context, var listener: RecyclerItemSongListenerListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>()  , Filterable {

    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private var isMoreLoading = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return if (viewType == VIEW_ITEM) {
            StudentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_playlist1, parent, false))
        } else {
            ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.progressbar, parent, false))
        }
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                val offerHolder = holder as StudentViewHolder
                bindStudentViewHolder(offerHolder, position)
            }
            else -> {
            }
        }
    }

    fun getItem(position: Int): Song {

        return fiteredList.get(position)
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

    fun bindStudentViewHolder(holder: StudentViewHolder, position: Int) {
        val data = getItem(position)
        holder.imgDot.setId(position)
        if (HomeActivity.isDataSaverEnabled()) run {

        } else {

            var playlistImage: String? = data.songImageUrl

            if (playlistImage!!.contains("/index.php")) {
                playlistImage = playlistImage.replace("/index.php", "")
            }
            if (playlistImage != null && !playlistImage.isEmpty()) {
                Glide.with(mContext).load(playlistImage)
                        .placeholder(R.drawable.ic_top_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(18)))
                        .into(holder.imgPlayList)
            }
        }
        holder.txtPlayListName.setText(""+data.getSongTitle())
        holder.txtPlayListContent.setText(""+data.getSongArtist())
        holder.tv_song_number.setVisibility(View.VISIBLE)
        holder.tv_song_number.setText("" + (position + 1))

        if (data.getIsLike()) {
            holder.iv_like_playlist_adapter.setImageResource(R.drawable.favourite_theme_filled)
        } else {
            holder.iv_like_playlist_adapter.setImageResource(R.drawable.favorite_theme_unfilled)
        }

//        if (data.from.toString().equals(AppConstants.FROM_MY_MUSIC) || data.getThirdparty_song()){
//            holder.iv_like_playlist_adapter.visibility= View.INVISIBLE
//        }else{
//            holder.iv_like_playlist_adapter.visibility= View.VISIBLE
//        }

        holder.iv_like_playlist_adapter.setId(position)
        holder.iv_like_playlist_adapter.setTag(holder.iv_like_playlist_adapter)


        when (data.getFrom()) {
            AppConstants.FROM_FAVOURITE_SONGS -> holder.imgDot.setVisibility(View.GONE)
            AppConstants.FROM_FAVOURITE_ALBUM -> {
            }
            AppConstants.FROM_FAVOURITE_PLAYLIST -> holder.imgDot.setVisibility(View.GONE)
            AppConstants.FROM_HOME_INTERNATIONAL ->{}
//                if (iv_favourite != null)
//                iv_favourite.setVisibility(View.GONE)
            AppConstants.FROM_MY_MUSIC -> {
            }
            AppConstants.FROM_UPLOAD_MUSIC,

            AppConstants.FROM_SEARCH_RESULT -> {
            }
        }

        holder.iv_like_playlist_adapter.setOnClickListener(View.OnClickListener { v ->
            val pos = v.id
            val selectedData = getItem(pos)
            if (data.type.equals(AppConstants.TABLE_DOWNLOADED_SONG, ignoreCase = true)) {
               Toast.makeText(mContext,mContext.getString(R.string.you_cant_perform_this_action),Toast.LENGTH_SHORT).show()
            }else {
                val image_like_adapter = v.tag as ImageView
                listener.onFavouriteButtonClicked(pos, selectedData, image_like_adapter)
                iv_static_star_LIke = image_like_adapter
            }



        })

        holder.imgDot.setOnClickListener(View.OnClickListener { v ->
            val id = v.id
            listener.onImageDotClicked(id,fiteredList.get(id))
        })
        holder.rl_main.setId(position)
        holder.rl_main.setOnClickListener(View.OnClickListener { v ->
            val id = v.id
           listener.onItemClicked(id,fiteredList)
        })
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
        return if (fiteredList.get(position) != null) VIEW_ITEM else VIEW_PROG
    }

    public fun refreshAdapter(updatedlist: ArrayList<Song>) {
        fiteredList = updatedlist
        list = updatedlist
        notifyDataSetChanged()
    }


}


class StudentViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
    var imgPlayList: ImageView = v.iv_song_image
    var txtPlayListName: TextView = v.tv_song_name
    var txtPlayListContent: TextView = v.txtSongContent
    var imgDot: ImageView = v.imgDot
    var llRowRoot: LinearLayout = v.llRowRoot
    var rl_main: RelativeLayout = v.rl_main
    var iv_like_playlist_adapter: ImageView = v.iv_like_playlist_adapter
    var tv_song_number: TextView = v.tv_song_number
}


class ProgressViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
    var pBar: ProgressBar

    init {
        pBar = v.findViewById(R.id.progressBar1) as ProgressBar
    }
}

