package com.music.ca7s.contant

import com.music.ca7s.model.genre_list.GenreDatum


interface RecylerViewItemClickListener {
    fun onTopCa7Clicked(position:Int,selectedData: GenreDatum)
    fun onNewRelase(position:Int,selectedData: GenreDatum)
    fun onRisingStar(position:Int,selectedData: GenreDatum)
}