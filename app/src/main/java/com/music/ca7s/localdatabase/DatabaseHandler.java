package com.music.ca7s.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.music.ca7s.R;
import com.music.ca7s.fragment.ViewPagerDownloadPlaylistFragment;
import com.music.ca7s.mediaplayer.Song;
import com.music.ca7s.model.DownloadPlaylistModel;
import com.music.ca7s.model.OfflineSearchModel;
import com.music.ca7s.utils.AppConstants;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import static com.music.ca7s.utils.AppConstants.*;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "casevensdatabase";
    private Context mCOntext ;

    //CartItems
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mCOntext = context;
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DOWNLOAD_SONG_TABLE = "CREATE TABLE " + TABLE_DOWNLOADED_SONG + "(" + SONG_NUMBER + " INTEGER PRIMARY KEY," + SONG_ID + " TEXT," + SONG_TITLE
                + " TEXT," + SONG_ALBUM + " TEXT," + SONG_URL + " TEXT," + SONG_IMAGE_URL + " TEXT," + IS_LIKE + " TEXT," + IS_FAVOURITE
                + " TEXT," + IS_DOWNLOAD + " TEXT," + TRACK_TYPE + " TEXT," + ALBUM_ID + " TEXT," + FROM + " TEXT,"
                + SONG_PATH + " TEXT," + IS_PLAYLIST + " TEXT," + LIKE_COUNT + " TEXT," + CREATED_AT + " TEXT," + LYRICS +
                " TEXT," + SONG_ARTIST + " TEXT," + SONG_IMAGE_PATH + " TEXT," + IS_INTERNATIONAL + " TEXT" + ")";

        String CREATE_DOWNLOAD_PLAYLIST_TABLE = "CREATE TABLE " + TABLE_DOWNLOADED_PLAYLIST_SONG + "(" + SONG_NUMBER + " INTEGER PRIMARY KEY," + SONG_ID + " TEXT," + SONG_TITLE
                + " TEXT," + SONG_ALBUM + " TEXT," + SONG_URL + " TEXT," + SONG_IMAGE_URL + " TEXT," + IS_LIKE + " TEXT," + IS_FAVOURITE
                + " TEXT," + IS_DOWNLOAD + " TEXT," + TRACK_TYPE + " TEXT," + ALBUM_ID + " TEXT," + FROM + " TEXT,"
                + SONG_PATH + " TEXT," + IS_PLAYLIST + " TEXT," + LIKE_COUNT + " TEXT," + CREATED_AT + " TEXT," + LYRICS +
                " TEXT," + SONG_ARTIST + " TEXT," + SONG_IMAGE_PATH + " TEXT, " +  IS_INTERNATIONAL + " TEXT" +")";

        String CREATE_OFFLINE_SEARCH_TABLE = "CREATE TABLE " + TABLE_OFFLINE_SEARCH + "(" + PLAYLIST_ID + " INTEGER," + PLAYLIST_NAME + " TEXT," + CREATED_AT + " TEXT" + ")";

        String CREATE_PLAYLIST_TABLE = "CREATE TABLE " + TABLE_PLAYLIST + "(" + PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PLAYLIST_NAME + " TEXT," + PLAYLIST_IMAGE
                + " TEXT, " + PLAYLIST_VALUES + " TEXT, " + CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_PLAYLIST_TABLE);
        db.execSQL(CREATE_DOWNLOAD_SONG_TABLE);
        db.execSQL(CREATE_DOWNLOAD_PLAYLIST_TABLE);
        db.execSQL(CREATE_OFFLINE_SEARCH_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOADED_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOADED_PLAYLIST_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE_SEARCH);
        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addSongItems(ArrayList<Song> contact, String KEY) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < contact.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(SONG_NUMBER, contact.get(i).getSongNumber()); // Beer Image Number
            values.put(SONG_ID, contact.get(i).getSongID()); // Beer Bottle size
            values.put(SONG_TITLE, contact.get(i).getSongTitle()); // Beer Name Name
            values.put(SONG_ALBUM, contact.get(i).getSongAlbum()); // Beer Image
            values.put(SONG_URL, contact.get(i).getSongURL()); // Beer Price
            values.put(SONG_IMAGE_URL, contact.get(i).getSongImageUrl()); // Beer Quantity
            values.put(IS_LIKE, contact.get(i).getIsLike());
            values.put(IS_FAVOURITE, contact.get(i).getIsFavourite());
            values.put(IS_DOWNLOAD, contact.get(i).getIsDownload());
            values.put(TRACK_TYPE, contact.get(i).getTrackType());
            values.put(ALBUM_ID, contact.get(i).getAlbumID()); // Beer Image
            values.put(FROM, contact.get(i).getFrom()); // Beer Price
            values.put(SONG_PATH, contact.get(i).getSongPath()); // Beer Quantity
            values.put(IS_PLAYLIST, contact.get(i).getIsPlaylist());
            values.put(LIKE_COUNT, contact.get(i).getLikeCount());
            values.put(CREATED_AT, contact.get(i).getCreatedAt());
            values.put(LYRICS, contact.get(i).getLyrics());
            values.put(SONG_ARTIST, contact.get(i).getSongArtist());
            values.put(SONG_IMAGE_PATH, contact.get(i).getSongImagePath());
            values.put(IS_INTERNATIONAL,contact.get(i).getThirdparty_song());
            // Inserting Row
            if (KEY.toString().equals(TABLE_DOWNLOADED_SONG)) {
                db.insert(TABLE_DOWNLOADED_SONG, null, values);
            } else if (KEY.toString().equals(TABLE_DOWNLOADED_PLAYLIST_SONG)) {
                db.insert(TABLE_DOWNLOADED_PLAYLIST_SONG, null, values);
            }
        }
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to add the new contact
    public void addPlaylist(DownloadPlaylistModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYLIST_NAME, contact.getName());
        values.put(PLAYLIST_IMAGE, contact.getImage());
        values.put(PLAYLIST_VALUES, contact.getValue());
        values.put(CREATED_AT, contact.getCreatedAt());
        db.insert(TABLE_PLAYLIST, null, values);
        db.close(); // Closing database connection
        try{
            if (ViewPagerDownloadPlaylistFragment.downloadPlaylistFragment != null){
                ViewPagerDownloadPlaylistFragment.downloadPlaylistFragment.setData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // code to get the single contact
    public DownloadPlaylistModel getPlaylistData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLAYLIST, new String[]{PLAYLIST_ID, PLAYLIST_NAME, PLAYLIST_IMAGE, PLAYLIST_VALUES, CREATED_AT
                }, PLAYLIST_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        DownloadPlaylistModel beerItem = new DownloadPlaylistModel();
        beerItem.setId(cursor.getInt(0));
        beerItem.setName(cursor.getString(1));
        beerItem.setImage(cursor.getString(2));
        beerItem.setValue(cursor.getString(3));
        beerItem.setCreatedAt(cursor.getString(4));
        // return contact
        return beerItem;
    }

    // code to get all contacts in a list view
    public ArrayList<DownloadPlaylistModel> getAllPLaylist() {
        ArrayList<DownloadPlaylistModel> songItemList = new ArrayList<DownloadPlaylistModel>();
        // Select All Query
        String selectQuery = null;
        selectQuery = "SELECT  * FROM " + TABLE_PLAYLIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try{
        if (selectQuery != null && cursor.moveToLast()) {
            do {
                DownloadPlaylistModel beerItem = new DownloadPlaylistModel();
                beerItem.setId(cursor.getInt(0));
                beerItem.setName(cursor.getString(1));
                beerItem.setImage(cursor.getString(2));
                beerItem.setValue(cursor.getString(3));
                beerItem.setCreatedAt(cursor.getString(4));
                songItemList.add(beerItem);
//                Log.e("getAllPLaylist : ", "  =  " + new Gson().toJson(songItemList));
            } while (cursor.moveToPrevious());
        }
        }finally{
            cursor.close();
        }

        // return contact list
        return songItemList;
    }

    // code to update the single contact
    public int updatePlaylist(DownloadPlaylistModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYLIST_ID, contact.getId());
        values.put(PLAYLIST_NAME, contact.getName());
        values.put(PLAYLIST_IMAGE, contact.getImage());
        values.put(PLAYLIST_VALUES, contact.getValue());
        values.put(CREATED_AT, contact.getCreatedAt());
        return db.update(TABLE_PLAYLIST, values, PLAYLIST_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});

    }



    // Deleting single contact
    public void deletePlaylist(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST, PLAYLIST_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    // Deleting complete table data
    public void deleteAllPlaylist() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PLAYLIST);
        db.close();
    }

    //#####################################################3
    // code to add the new contact
    public void addOfflinelist(OfflineSearchModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (doesTableExist(db,TABLE_OFFLINE_SEARCH)){
            String searchName = getOfflineSearch_name(contact.getName());
//            Log.e("SearchName : : "," = "+searchName);
            if (searchName != null && !searchName.isEmpty() && searchName.toString().equalsIgnoreCase(contact.getName())) {
                updateOfflineSearch(contact);
            } else {
                values.put(PLAYLIST_ID,contact.getId());
                values.put(PLAYLIST_NAME, contact.getName());
                values.put(CREATED_AT, contact.getCreatedAt());
                db.insert(TABLE_OFFLINE_SEARCH, null, values);
//                Log.e("addOFFLINE : ", "  =  " + new Gson().toJson(contact));
            }
        }
        else {
                values.put(PLAYLIST_ID,contact.getId());
                values.put(PLAYLIST_NAME, contact.getName());
                values.put(CREATED_AT, contact.getCreatedAt());
                db.insert(TABLE_OFFLINE_SEARCH, null, values);
//                Log.e("addOFFLINE : ", "  =  " + new Gson().toJson(contact));
        }
        //2nd argument is String containing nullColumnHack
               db.close(); // Closing database connection
    }

    public boolean CheckIsDataAlreadyInDBorNot(String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OFFLINE_SEARCH, new String[]{PLAYLIST_ID, PLAYLIST_NAME, CREATED_AT
                }, PLAYLIST_NAME + "=?", new String[]{fieldValue}, null, null, null, null);
        if (cursor != null){
            return true;
        }else {
           return false;
        }
//        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
//        try {
//            Cursor cursor = db.rawQuery(Query, null);
//            if (cursor.getCount() <= 0) {
//                cursor.close();
//                return false;
//            }
//            cursor.close();
//        }catch (SQLiteException exception){
//            Log.e("SQLiteException : ", exception.getMessage()+"");
//            return false;
//        }
//        return true;
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    // code to get the single contact
    public String getOfflineSearch_name(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OFFLINE_SEARCH, new String[]{PLAYLIST_ID, PLAYLIST_NAME, CREATED_AT
                }, PLAYLIST_NAME + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

//        Log.e("Cursor : ,","  =   "+cursor.toString());
        assert cursor != null;
        if (cursor.getColumnCount()>0) {
//            OfflineSearchModel beerItem = new OfflineSearchModel();
//            beerItem.setId(cursor.getInt(0));
//            beerItem.setName(cursor.getString(1));
//            beerItem.setCreatedAt(cursor.getString(2));
            String name = "";
            try{
                name = cursor.getString(1);
            }catch (CursorIndexOutOfBoundsException ex){
                name = "";
            }

            // return contact
            return name;
        }
        else {
            return  "";
        }
    }

    // code to get all contacts in a list view
    public ArrayList<OfflineSearchModel> getAllOfflineSearch() {
        ArrayList<OfflineSearchModel> songItemList = new ArrayList<OfflineSearchModel>();
        // Select All Query
        String selectQuery = null;
        selectQuery = "SELECT  * FROM " + TABLE_OFFLINE_SEARCH;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (selectQuery != null && cursor.moveToFirst()) {
            do {
                OfflineSearchModel beerItem = new OfflineSearchModel();
                beerItem.setId(cursor.getInt(0));
                beerItem.setName(cursor.getString(1));
                beerItem.setCreatedAt(cursor.getString(2));
                if (beerItem.getName() != null && !beerItem.getName().isEmpty()) {
                    songItemList.add(beerItem);
                }
//                Log.e("getOfflineSearch : ", "  =  " + new Gson().toJson(songItemList));
            } while (cursor.moveToNext());
        }

        // return contact list
        return songItemList;
    }

    // code to update the single contact
    public int updateOfflineSearch(OfflineSearchModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYLIST_ID, contact.getId());
        values.put(PLAYLIST_NAME, contact.getName());
        values.put(CREATED_AT, contact.getCreatedAt());

        return db.update(TABLE_OFFLINE_SEARCH, values, PLAYLIST_NAME + " = ?",
                new String[]{String.valueOf(contact.getName())});

    }

    // Deleting single contact
    public void deleteOfflineSearch(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OFFLINE_SEARCH, PLAYLIST_NAME + " = ?",
                new String[]{id});

        db.close();
    }

    // Deleting complete table data
    public void deleteAllOfflineSearch() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_OFFLINE_SEARCH);
        db.close();
    }


    //##########################################################

    // code to add the new contact
    public void addSongItem(Song contact, String KEY) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SONG_NUMBER, contact.getSongNumber()); // Beer Image Number
        values.put(SONG_ID, contact.getSongID()); // Beer Bottle size
        values.put(SONG_TITLE, contact.getSongTitle()); // Beer Name Name
        values.put(SONG_ALBUM, contact.getSongAlbum()); // Beer Image
        values.put(SONG_URL, contact.getSongURL()); // Beer Price
        values.put(SONG_IMAGE_URL, contact.getSongImageUrl()); // Beer Quantity
        values.put(IS_LIKE, contact.getIsLike());
        values.put(IS_FAVOURITE, contact.getIsFavourite());
        values.put(IS_DOWNLOAD, contact.getIsDownload());
        values.put(TRACK_TYPE, contact.getTrackType());
        values.put(ALBUM_ID, contact.getAlbumID()); // Beer Image
        values.put(FROM, contact.getFrom()); // Beer Price
        values.put(SONG_PATH, contact.getSongPath()); // Beer Quantity
        values.put(IS_PLAYLIST, contact.getIsPlaylist());
        values.put(LIKE_COUNT, contact.getLikeCount());
        values.put(CREATED_AT, contact.getCreatedAt());
        values.put(LYRICS, contact.getLyrics());
        values.put(SONG_ARTIST, contact.getSongArtist());
        values.put(SONG_IMAGE_PATH, contact.getSongImagePath());
//        values.put(IS_INTERNATIONAL,contact.getThirdparty_song());
        // Inserting Row
        if (KEY.toString().equals(TABLE_DOWNLOADED_SONG)) {
            db.insert(TABLE_DOWNLOADED_SONG, null, values);
        } else if (KEY.toString().equals(TABLE_DOWNLOADED_PLAYLIST_SONG)) {
            db.insert(TABLE_DOWNLOADED_PLAYLIST_SONG, null, values);
        }

//        Log.e("addSongItem : ", "  =  " + new Gson().toJson(contact));

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public Song getSong(int id, String KEY) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (KEY.toString().equals(TABLE_DOWNLOADED_SONG)) {
            cursor = db.query(TABLE_DOWNLOADED_SONG, new String[]{SONG_NUMBER, SONG_ID, SONG_TITLE, SONG_ALBUM, SONG_URL, SONG_IMAGE_URL, IS_LIKE, IS_FAVOURITE
                            , IS_DOWNLOAD, TRACK_TYPE, ALBUM_ID, FROM, SONG_PATH, IS_PLAYLIST, LIKE_COUNT, CREATED_AT, LYRICS, SONG_ARTIST, SONG_IMAGE_PATH,IS_INTERNATIONAL}, SONG_NUMBER + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
        } else if (KEY.toString().equals(TABLE_DOWNLOADED_PLAYLIST_SONG)) {
            cursor = db.query(TABLE_DOWNLOADED_PLAYLIST_SONG, new String[]{SONG_NUMBER, SONG_ID, SONG_TITLE, SONG_ALBUM, SONG_URL, SONG_IMAGE_URL, IS_LIKE, IS_FAVOURITE
                            , IS_DOWNLOAD, TRACK_TYPE, ALBUM_ID, FROM, SONG_PATH, IS_PLAYLIST, LIKE_COUNT, CREATED_AT, LYRICS, SONG_ARTIST, SONG_IMAGE_PATH,IS_INTERNATIONAL}, SONG_NUMBER + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
        }
        if (cursor != null)
            cursor.moveToFirst();
        Song beerItem = new Song();
        beerItem.setSongNumber(cursor.getString(0));
        beerItem.setSongID(cursor.getString(1));
        beerItem.setSongTitle(cursor.getString(2));
        beerItem.setSongAlbum(cursor.getString(3));
        beerItem.setSongURL(cursor.getString(4));
        beerItem.setSongImageUrl(cursor.getString(5));
        beerItem.setIsLike(Boolean.parseBoolean(cursor.getString(6)));
        beerItem.setIsFavourite(Boolean.parseBoolean(cursor.getString(7)));
        beerItem.setIsDownload(Boolean.parseBoolean(cursor.getString(8)));
        beerItem.setTrackType(cursor.getString(9));
        beerItem.setAlbumID(cursor.getString(10));
        beerItem.setFrom(cursor.getString(11));
        beerItem.setSongPath(cursor.getString(12));
        beerItem.setIsPlaylist(Boolean.parseBoolean(cursor.getString(13)));
        beerItem.setLikeCount(cursor.getInt(14));
        beerItem.setCreatedAt(cursor.getString(15));
        beerItem.setLyrics(cursor.getString(16));
        beerItem.setSongArtist(cursor.getString(17));
        beerItem.setSongImagePath(cursor.getString(18));
//        beerItem.setThirdparty_song(Boolean.parseBoolean(cursor.getString(19)));
        // return contact
        return beerItem;
    }


    // code to get all contacts in a list view
    public ArrayList<Song> getAllSong(String KEY) {
        ArrayList<Song> songItemList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = null;
        if (KEY.toString().equals(TABLE_DOWNLOADED_SONG)) {
            selectQuery = "SELECT  * FROM " + TABLE_DOWNLOADED_SONG;
        } else if (KEY.toString().equals(TABLE_DOWNLOADED_PLAYLIST_SONG)) {
            selectQuery = "SELECT  * FROM " + TABLE_DOWNLOADED_PLAYLIST_SONG;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (selectQuery != null && cursor.moveToFirst()) {
            do {
                Song beerItem = new Song();
                beerItem.setSongNumber(cursor.getString(0));
                beerItem.setSongID(cursor.getString(1));
                beerItem.setSongTitle(cursor.getString(2));
                beerItem.setSongAlbum(cursor.getString(3));
                beerItem.setSongURL(cursor.getString(4));
                beerItem.setSongImageUrl(cursor.getString(5));
                beerItem.setIsLike(Boolean.parseBoolean(cursor.getString(6)));
                beerItem.setIsFavourite(Boolean.parseBoolean(cursor.getString(7)));
                beerItem.setIsDownload(Boolean.parseBoolean(cursor.getString(8)));
                beerItem.setTrackType(cursor.getString(9));
                beerItem.setAlbumID(cursor.getString(10));
                beerItem.setFrom(cursor.getString(11));
                beerItem.setSongPath(cursor.getString(12));
                beerItem.setIsPlaylist(Boolean.parseBoolean(cursor.getString(13)));
                beerItem.setLikeCount(cursor.getInt(14));
                beerItem.setCreatedAt(cursor.getString(15));
                beerItem.setLyrics(cursor.getString(16));
                beerItem.setSongArtist(cursor.getString(17));
                beerItem.setSongImagePath(cursor.getString(18));
                beerItem.setType(TABLE_DOWNLOADED_SONG);
//                beerItem.setThirdparty_song(Boolean.parseBoolean(cursor.getString(19)));
                // Adding contact to list
                File myFile = new File(AppConstants.DEVICE_PATH + beerItem.getSongID() + AppConstants.TEMPRARY_MUSIC_EXTENTION + ".nomedia");
                if (myFile.exists()) {
                    songItemList.add(beerItem);
                }
//                Log.e("songItemList : ","  =  "+new Gson().toJson(cursor));
//                Log.e("songItemList : ", "  =  " + new Gson().toJson(songItemList));
            } while (cursor.moveToNext());
        }

        // return contact list
        return songItemList;
    }

    // code to update the single contact
    public int updateSongItem(Song contact, String KEY) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SONG_NUMBER, contact.getSongNumber()); // Beer Image Number
        values.put(SONG_ID, contact.getSongID()); // Beer Bottle size
        values.put(SONG_TITLE, contact.getSongTitle()); // Beer Name Name
        values.put(SONG_ALBUM, contact.getSongAlbum()); // Beer Image
        values.put(SONG_URL, contact.getSongURL()); // Beer Price
        values.put(SONG_IMAGE_URL, contact.getSongImageUrl()); // Beer Quantity
        values.put(IS_LIKE, contact.getIsLike());
        values.put(IS_FAVOURITE, contact.getIsFavourite());
        values.put(IS_DOWNLOAD, contact.getIsDownload());
        values.put(TRACK_TYPE, contact.getTrackType());
        values.put(ALBUM_ID, contact.getAlbumID()); // Beer Image
        values.put(FROM, contact.getFrom()); // Beer Price
        values.put(SONG_PATH, contact.getSongPath()); // Beer Quantity
        values.put(IS_PLAYLIST, contact.getIsPlaylist());
        values.put(LIKE_COUNT, contact.getLikeCount());
        values.put(CREATED_AT, contact.getCreatedAt());
        values.put(LYRICS, contact.getLyrics());
        values.put(SONG_ARTIST, contact.getSongArtist());
        values.put(SONG_IMAGE_PATH, contact.getSongImagePath());
//        values.put(IS_INTERNATIONAL,contact.getThirdparty_song());

        if (KEY.toString().equals(TABLE_DOWNLOADED_SONG)) {
            // updating row
            return db.update(TABLE_DOWNLOADED_SONG, values, SONG_NUMBER + " = ?",
                    new String[]{String.valueOf(contact.getSongNumber())});
        } else if (KEY.toString().equals(TABLE_DOWNLOADED_PLAYLIST_SONG)) {
            // updating row
            return db.update(TABLE_DOWNLOADED_PLAYLIST_SONG, values, SONG_NUMBER + " = ?",
                    new String[]{String.valueOf(contact.getSongNumber())});
        } else {
            return 0;
        }
    }

    // Deleting single contact
    public void deleteSongItem(Song contact, String KEY) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (KEY.toString().equals(TABLE_DOWNLOADED_SONG)) {
            db.delete(TABLE_DOWNLOADED_SONG, SONG_NUMBER + " = ?",
                    new String[]{String.valueOf(contact.getSongNumber())});
        } else if (KEY.toString().equals(TABLE_DOWNLOADED_PLAYLIST_SONG)) {
            db.delete(TABLE_DOWNLOADED_PLAYLIST_SONG, SONG_NUMBER + " = ?",
                    new String[]{String.valueOf(contact.getSongNumber())});
        }

        db.close();
    }

    // Deleting complete table data
    public void deleteAllBeerItem(@NotNull ArrayList<Song> items, String KEY) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (KEY.toString().equals(TABLE_DOWNLOADED_SONG)) {
            db.execSQL("delete from " + TABLE_DOWNLOADED_SONG);
        } else if (KEY.toString().equals(TABLE_DOWNLOADED_PLAYLIST_SONG)) {
            db.execSQL("delete from " + TABLE_DOWNLOADED_PLAYLIST_SONG);
        }
        db.close();
    }

    // Getting Beer Count
    public int getBeerItemCount(String KEY) {
        int count = 0;

        if (KEY.toString().equals(TABLE_DOWNLOADED_SONG)) {
            count = (count + getAllSong(KEY).size());
        } else if (KEY.toString().equals(TABLE_DOWNLOADED_PLAYLIST_SONG)) {
            count = (count + getAllSong(KEY).size());
        }
//        Log.e("Count Beer : ", KEY + "   = " + count + "  ");
        // return count
        return count;
    }

}
