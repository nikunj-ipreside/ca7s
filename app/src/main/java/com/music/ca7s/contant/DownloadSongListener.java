package com.music.ca7s.contant;

public interface DownloadSongListener {
    void changeProgress(String position, int progress);
    void onDownloadSuccess();
}
