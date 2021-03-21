package com.example.androidproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncThumbnailDownloader extends AsyncTask<String, Void, Bitmap> {

    private AppCompatActivity mainAct;

    public AsyncThumbnailDownloader(AppCompatActivity mainAct){
        this.mainAct = mainAct;
    }

    @Override
    protected Bitmap doInBackground(String... strings){

        URL url = null;

        try {
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(in);

                return bm;

            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Bitmap bm)
    {

        //Setting up the Imageview with the downloaded bitmap
        ImageView iv = (ImageView)mainAct.findViewById(R.id.cocktailThumbnail);
        iv.setImageBitmap(bm);

    }

}