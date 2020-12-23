package com.music.ca7s.contant

import com.music.ca7s.model.favourite.album.AlbumData

interface FavouriteRecylerViewItemClickListener {
    fun onCollectionClicked(position:Int,selectedData: AlbumData)
    fun onSongsClicked(position:Int,selectedData: AlbumData)
    fun onPlayListCollected(position:Int,selectedData: AlbumData)
}