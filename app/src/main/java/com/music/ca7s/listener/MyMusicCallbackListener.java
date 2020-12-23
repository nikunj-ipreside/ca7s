package com.music.ca7s.listener;

import com.music.ca7s.mediaplayer.Song;

import java.util.ArrayList;

public interface MyMusicCallbackListener {
    void onStartedMediaPlayer();
    void onChangeSeekposition(int totalprogress, int completedProgress);
    void onPlayMediaPlayer();

    void onStopMediaPlayer();

    void onPauseMediaPlayer();

    void onsetSongData(Song song, int audioIndex);

    void onListShuffle(ArrayList<Song> shuffledList);

    void setCurrenPosition(int audioIndex);

    void onUpdatePlayerList(int audioIndex,ArrayList<Song> audioLIst,String type);
}
