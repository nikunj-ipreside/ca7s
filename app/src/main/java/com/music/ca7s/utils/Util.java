package com.music.ca7s.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.music.ca7s.contant.SharedPref;
import com.yariksoffice.lingver.Lingver;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

import static com.music.ca7s.utils.AppConstants.DATE_FORMAT;

public class Util {

    public static void openOnWeb(Context context,String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static int getTimeinInt(long milliseconds){
        //Divide millis by 1000 to get the number of seconds.
        int seconds = (int) (milliseconds / 1000);
        return seconds;
    }

    public static String getSongTime(long miliseconds){
        String seconds = String.valueOf((int) (miliseconds / 1000) % 60) ;
        String minutes = String.valueOf((int) ((miliseconds / (1000*60)) % 60));
        if (seconds.length() == 1){
            seconds="0"+seconds;
        }
        if (minutes.length() == 1){
            minutes="0"+minutes;
        }
        String totalString = minutes+":"+seconds;
          return totalString;

    }

    public static String getCurrentDateTime() {
        String dateTime = String.valueOf(DateFormat.format(DATE_FORMAT, System.currentTimeMillis()));
        return dateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String changeLanguage(Context baseContext) {
      TutorialPrefrences tutorialPrefrences = new TutorialPrefrences(baseContext);
      String selectedLAnguage = tutorialPrefrences.getString(SharedPref.LANGUAGE);
      String language =Locale.getDefault().getLanguage();
        if (selectedLAnguage.toString().equalsIgnoreCase("Portuguese")){
            language = "pt";
        }else if (selectedLAnguage.toString().equalsIgnoreCase("English")){
            language = "en";
        }
        Lingver.getInstance().setLocale(baseContext, language);
        return selectedLAnguage;

    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
    }

    public static String getPathFromBitmap(Context context,Bitmap bitmap){
        String imageURL = "";
        OutputStream fOut = null;
        File imagedirect = new File(context.getExternalCacheDir() + "/Images.nomedia" + System.currentTimeMillis() + ".png");
        try {
            fOut = new FileOutputStream(imagedirect);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver()
                    , imagedirect.getAbsolutePath(), imagedirect.getName(), imagedirect.getName());
            imageURL = imagedirect.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageURL;
    }

    public static Bitmap convertBase64ToBitmap(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String getBase64FronPath(String songPic) {
        File file = new File(songPic);  //file Path
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            for (int j = 0; j < b.length; j++) {
                System.out.print((char) b[j]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }

        byte[] byteFileArray = new byte[0];
        try {
            byteFileArray = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String base64String = "";
        if (byteFileArray.length > 0) {
            base64String = android.util.Base64.encodeToString(byteFileArray, Base64.DEFAULT);
//            Log.i("File Base64 string", "IMAGE PARSE ==>" + base64String);
        }
        return base64String;
    }

    public static void writeToFile(Context context,String data) {
        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DCIM + "/YourFolder/"
                        );

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, "config.txt");

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
