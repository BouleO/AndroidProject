package com.example.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AsyncCocktailSearch extends AsyncTask<String, Void, JSONObject> {

    private AppCompatActivity mainAct;

    public AsyncCocktailSearch(AppCompatActivity mainAct){
        this.mainAct = mainAct;
    }

    @Override
    protected JSONObject doInBackground(String... strings){

        URL url = null;

        String s;

        try {
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                s = readStream(in);

                JSONObject j = new JSONObject(s);

                return j;

            } catch (JSONException e) {
                e.printStackTrace();
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

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(JSONObject j)
    {

        //Setting up the listview with a custom bitmap adapter (including an imageview filled with the bitmap and a text field for the name)
        ListView list = (ListView)mainAct.findViewById(R.id.listView);
        BitmapAdapter t = new BitmapAdapter(list.getContext());
        list.setAdapter(t);

        try {
            //retrieve data from JSON, separate into items
            JSONArray array = j.getJSONArray("drinks");
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> ids = new ArrayList<>();

            for(int i = 0; i<array.length(); i++){

                //separate into objects
                JSONObject temp = array.getJSONObject(i);
                String cocktailName = temp.getString("strDrink");
                String imageUrl = temp.getString("strDrinkThumb");
                String cocktailID = temp.getString("idDrink");
                names.add(cocktailName);
                ids.add(cocktailID);

                AsyncBitmapDownloader asyncBitmapDownloader = new AsyncBitmapDownloader(t,cocktailName);
                asyncBitmapDownloader.execute(imageUrl);

            }

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Sending the adapter, the name and the url to the asynctask
                    Intent cocktailIntent = new Intent();
                    cocktailIntent.setClass(mainAct.getApplicationContext(), CocktailActivity.class);
                    cocktailIntent.putExtra("cocktailID",ids.get(position));
                    mainAct.startActivity(cocktailIntent);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
