package com.music.ca7s.adapter.homeadapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.contant.RecylerViewSongItemClickListener
import com.music.ca7s.mediaplayer.Song
import java.util.*
import android.R.attr.data
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.music.ca7s.R
import com.music.ca7s.helper.ItemTouchHelperAdapter
import com.music.ca7s.helper.ItemTouchHelperViewHolder
import com.music.ca7s.helper.OnStartDragListener
import com.music.ca7s.mediaplayer.StorageUtil
import java.util.logging.Handler


class NowPlayingSongsAdapter(var list: ArrayList<Song>, var mContext: Context, var listener: RecylerViewSongItemClickListener,val startDragListener: OnStartDragListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NowPlayingViewHolder(inflater, parent)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val movie: Song = list[position]
        (holder as NowPlayingViewHolder).bind(movie, mContext)
        (holder as NowPlayingViewHolder).itemView.tag = list[position]
        (holder as NowPlayingViewHolder).itemView.id = position

        (holder as NowPlayingViewHolder).iv_dot!!.tag = (holder as NowPlayingViewHolder)
        (holder as NowPlayingViewHolder).iv_dot!!.id = position
        (holder as NowPlayingViewHolder).itemView.setOnClickListener(View.OnClickListener {
            val source = it.tag as Song
            val id = it.id
            val storageUtil = StorageUtil(mContext)
            listener.onSongSelected(id, source,storageUtil.loadTYpe())
        })

//        if (position == 0){
//            (mContext as HomeActivity).updateUpnextList(list)
//        }


//        (holder as NowPlayingViewHolder).iv_dot!!.setOnClickListener(View.OnClickListener {
//            val source = it.tag as Song
//            val id = it.id
//            listener.onDotSelected(id, source)
//        })


        // Start a drag whenever the handle view it touched
        (holder as NowPlayingViewHolder).iv_dot!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val viewholder = v.tag as NowPlayingViewHolder
            startDragListener.onStartDrag(viewholder)
            false
        })

    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        try {
            if (fromPosition < 0) {
            }else{
                val myNewSong: Song = list.get(fromPosition)
                list.removeAt(fromPosition)
                list.add(toPosition,myNewSong)
                notifyDataSetChanged()
//                notifyItemMoved(fromPosition, toPosition)
//            android.os.Handler().postDelayed(Runnable {
//                if (fromPosition > 0) {
//                Collections.swap(list, fromPosition, toPosition)
                startDragListener.onNoteListChanged(list)
//                }
//            },1500)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onItemDismiss(position: Int) {

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

    public fun refreshAdapter(updatedlist: ArrayList<Song>) {
        list = updatedlist
        notifyDataSetChanged()
    }



    open class NowPlayingViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.row_now_playing, parent, false)), ItemTouchHelperViewHolder {

        public var tv_data_name: TextView? = null
        public var iv_image: ImageView? = null
        public var iv_dot: ImageView? = null

        init {
            tv_data_name = itemView.findViewById(R.id.tv_song_name)
            iv_image = itemView.findViewById(R.id.iv_song_image)
            iv_dot = itemView.findViewById(R.id.imgDot)
        }

        override fun onItemClear() {
//            itemView.setBackgroundColor(0)
        }

        override fun onItemSelected() {
//            itemView.setBackgroundColor(Color.GRAY)
        }

        fun bind(countryListData: Song, mContext: Context) {
            tv_data_name?.text = countryListData.songTitle + " - " + countryListData.songArtist

            if (countryListData.songImageUrl != null && !countryListData.songImageUrl.isEmpty()) {
                if (HomeActivity.isDataSaverEnabled()) run {
                    Glide.with(mContext)
                            .load(R.drawable.ic_top_placeholder)
                            .transition(withCrossFade())
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                            .placeholder(R.drawable.ic_top_placeholder)
                            .error(R.drawable.ic_top_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(iv_image!!)
                } else {
                    if ((countryListData.songImageUrl != null) and !countryListData.songImageUrl.isEmpty() && iv_image != null) {
                        var playlistImage: String? = countryListData.songImageUrl
                        if (playlistImage!!.contains("/index.php")) {
                            playlistImage = playlistImage.replace("/index.php", "")
                        }
                        if (playlistImage != null && !playlistImage.isEmpty()) {
                            Glide.with(mContext)
                                    .load(playlistImage)
                                    .transition(withCrossFade())
                                    .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                                    .placeholder(R.drawable.ic_top_placeholder)
                                    .error(R.drawable.ic_top_placeholder)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .into(iv_image!!)
                        }else{
                            Glide.with(mContext)
                                    .load(R.drawable.ic_top_placeholder)
                                    .transition(withCrossFade())
                                    .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                                    .placeholder(R.drawable.ic_top_placeholder)
                                    .error(R.drawable.ic_top_placeholder)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .into(iv_image!!)
                        }
                    }
//                    (mContext as HomeActivity).loadImage(countryListData.songImageUrl, iv_image)
                }
            }else{
                Glide.with(mContext)
                        .load(R.drawable.ic_top_placeholder)
                        .transition(withCrossFade())
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                        .placeholder(R.drawable.ic_top_placeholder)
                        .error(R.drawable.ic_top_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(iv_image!!)
            }
        }

    }

}


