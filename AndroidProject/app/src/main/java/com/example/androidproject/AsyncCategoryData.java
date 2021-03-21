package com.example.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class AsyncCategoryData extends AsyncTask<String, Void, JSONObject> {

    private AppCompatActivity mainAct;

    public AsyncCategoryData(AppCompatActivity mainAct){
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

        ArrayList<String> categories = new ArrayList<String>();

        try {
            //retrieve data from JSON, separate into items
            JSONArray array = j.getJSONArray("drinks");

            for(int i = 0; i<array.length(); i++){

                //separate into objects
                JSONObject temp = array.getJSONObject(i);
                categories.add(temp.getString("strCategory"));

                //setting up the list view with an array adapter
                ListView lvCategories = (ListView)mainAct.findViewById(R.id.CategoriesListView);
                ArrayAdapter categoriesAdapter = new ArrayAdapter(mainAct, android.R.layout.simple_list_item_1,categories);

                lvCategories.setAdapter(categoriesAdapter);

                lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Sending the correct category to the cocktailList activity
                        Intent cocktailListIntent = new Intent();
                        cocktailListIntent.setClass(mainAct.getApplicationContext(), CocktailListActivity.class);
                        cocktailListIntent.putExtra("category",categories.get(position));
                        mainAct.startActivity(cocktailListIntent);
                        //Toast.makeText(mainAct, "success", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mainAct, "error", Toast.LENGTH_SHORT);
        }

    }


}
