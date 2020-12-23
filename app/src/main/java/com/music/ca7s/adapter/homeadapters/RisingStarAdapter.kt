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
import com.music.ca7s.contant.RecylerViewItemClickListener
import com.music.ca7s.fragment.BaseFragment
import com.music.ca7s.model.genre_list.GenreDatum
import com.music.ca7s.model.genre_list.GenrePojo
import com.music.ca7s.model.topcas.TopCasData

class RisingStarAdapter(private var list: List<GenreDatum>, public var mContext: Context,var listener : RecylerViewItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            return RisingViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val movie: GenreDatum = list[position]
            (holder as RisingViewHolder).bind(movie, mContext)
        if (position == 0){
            Glide.with(mContext)
                    .load(R.drawable.ic_rising)
                    .placeholder(R.drawable.ic_rising)
                    .error(R.drawable.ic_rising)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(250,250)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(32)))
                    .into((holder as RisingViewHolder).iv_image!!)
        }
            (holder as RisingViewHolder).itemView.tag = list[position]
            (holder as RisingViewHolder).itemView.id = position
            (holder as RisingViewHolder).itemView.setOnClickListener(View.OnClickListener {
                val source = it.tag as GenreDatum
                val id = it.id
                    listener.onRisingStar(id, source)
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

    public fun refreshAdapter(updatedlist: List<GenreDatum>)
    {
        list=updatedlist
        notifyDataSetChanged()
    }

}

class RisingViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.item_topca7, parent, false)) {

    public var tv_data_name: TextView? = null
    public var iv_image: ImageView? = null
    public var iv_image_background:ImageView?=null
    init {
        tv_data_name = itemView.findViewById(R.id.tv_name)
        iv_image = itemView.findViewById(R.id.iv_image)
        iv_image_background=itemView.findViewById(R.id.iv_image_background)
    }

    fun bind(countryListData: GenreDatum, mContext: Context) {
        tv_data_name?.text=countryListData.type

        if (countryListData.imageIcon != null && !countryListData.imageIcon.isEmpty()){
            if (HomeActivity.isDataSaverEnabled()) run {

            } else {
                (mContext as HomeActivity).loadImage(countryListData.imageIcon, iv_image)
            }
        }
    }
}