package com.music.ca7s.contant

import com.music.ca7s.mediaplayer.Song
import com.music.ca7s.model.genre_list.GenreDatum


interface RecylerViewSongItemClickListener {
    fun onSongSelected(position:Int,selectedData: Song,type:String)
    fun onDotSelected(position:Int,selectedData: Song)
    fun onImageSelected(position:Int,selectedData: Song)

}