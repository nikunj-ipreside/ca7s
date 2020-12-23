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
import com.music.ca7s.R
import com.music.ca7s.activity.HomeActivity
import com.music.ca7s.contant.RecylerViewItemClickListener
import com.music.ca7s.fragment.BaseFragment
import com.music.ca7s.model.genre_list.GenreDatum
import com.music.ca7s.model.genre_list.GenrePojo
import com.music.ca7s.model.topcas.TopCasData

class CatTopAdapter(private var list: List<GenreDatum>, public var mContext: Context,var listener : RecylerViewItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            return ShoppingViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val movie: GenreDatum = list[position]
            (holder as ShoppingViewHolder).bind(movie, mContext)
        if (position == 0){
//            Glide.with(mContext)
//                    .load(R.drawable.ic_top_placeholder)
//                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                    .override(250,250)
//                    .into((holder as ShoppingViewHolder).iv_image!!)
//            (holder as ShoppingViewHolder).iv_image?.setImageResource(R.drawable.ic_top_placeholder)
//            (holder as ShoppingViewHolder).iv_image_background?.visibility=View.GONE
//            (holder as ShoppingViewHolder).tv_data_name?.text=""
        }
            (holder as ShoppingViewHolder).itemView!!.tag = list[position]
            (holder as ShoppingViewHolder).itemView!!.id = position
            (holder as ShoppingViewHolder).itemView!!.setOnClickListener(View.OnClickListener {
                val source = it.tag as GenreDatum
                val id = it.id
                    listener.onTopCa7Clicked( id, source)
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

class ShoppingViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
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
