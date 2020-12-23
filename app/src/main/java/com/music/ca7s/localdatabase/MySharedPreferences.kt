package com.music.ca7s.localdatabase;

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MySharedPreferences() {

    public fun setBooleanValue(key: String, value: Boolean) {
        mPrefEditor!!.putBoolean(key, value).commit()
    }

    public fun getBooleanValue(key: String): Boolean {
        val isNull = mySharedPreferences!!.getBoolean(key, false) != false
        return if (isNull) mySharedPreferences!!.getBoolean(key, false) else false
    }

    public fun setStringValue(key: String, value: String) {
        mPrefEditor!!.putString(key, value).commit()
    }

    public fun getStringValue(key: String): String {
        val isNull = mySharedPreferences!!.getString(key, "") != null
//        Log.e("GetStringValue : ",mySharedPreferences!!.getString(key,""))
        return if (isNull) mySharedPreferences!!.getString(key, "")!! else ""
    }

    public fun setIntValue(key: String, value: Int) {
        mPrefEditor!!.putInt(key, value).commit()
    }

    public fun setList(key: String, listValue: Int) {
        val gson = Gson()
        val mySavedList=getCampIdList(key)
        mySavedList.add(listValue)
        val value: String = gson.toJson(mySavedList)
        mPrefEditor!!.putString(key, value).commit()
    }

    public fun getCampIdList(key: String): ArrayList<Int> {
        val isNull = mySharedPreferences!!.getString(key, "") != null

        if (isNull) {
            val value: String = mySharedPreferences!!.getString(key, "")!!

            val myList: ArrayList<Int> = Gson().fromJson(value, object : TypeToken<List<Int>>() {

            }.type);
            return myList
        } else {
           return ArrayList<Int>()
        }
    }



    public fun clearSharedPreferences() {
        mPrefEditor!!.clear().commit()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: MySharedPreferences? = null
        private var mySharedPreferences: SharedPreferences? = null
        private final val sharedFile = "org.gws.sleeman"
        private var mPrefEditor: SharedPreferences.Editor? = null
        val gson = Gson()
        @SuppressLint("CommitPrefEdits")
        fun getInstance(context: Context): MySharedPreferences {
//            if (instance == null) {
            instance = MySharedPreferences()
            mySharedPreferences = context.getSharedPreferences(sharedFile, MODE_PRIVATE)
            mPrefEditor = mySharedPreferences!!.edit()
            return instance!!
//            }
//
//            return instance!!
        }
    }


}