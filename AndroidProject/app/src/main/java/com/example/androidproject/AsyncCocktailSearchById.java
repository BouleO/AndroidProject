package com.example.androidproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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

public class AsyncCocktailSearchById extends AsyncTask<String, Void, JSONObject> {

    private AppCompatActivity mainAct;

    public AsyncCocktailSearchById(AppCompatActivity mainAct){
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
                Log.i("cocktailDetails",j.toString());

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

        try {
            //retrieve data from JSON, separate into items
            JSONArray array = j.getJSONArray("drinks");

            JSONObject temp = array.getJSONObject(0);

            //Setting up the textviews with the information returned by the api
            TextView nameTv = (TextView)mainAct.findViewById(R.id.cocktailNameTextView);
            nameTv.setText("\n"+temp.getString("strDrink")+"\n\n");

            TextView instructionsTv = (TextView)mainAct.findViewById(R.id.cocktailInstructionTextView);
            instructionsTv.setText("\n"+temp.getString("strInstructions")+"\n\n");

            //Ingredients formatting
            String ingredients = "";
            for(int i = 1; i<16; i++)
            {
                String ingredient = null;
                ingredient = temp.getString("strIngredient"+i);
                if(ingredient != "null"){
                    ingredients += (ingredient +"  "+ temp.getString("strMeasure"+i) + "\n");
                }

            }
            TextView ingredientsTv = (TextView)mainAct.findViewById(R.id.cocktailIngredientsTextView);
            ingredientsTv.setText("\n"+ingredients);

            //Loading the thumbnail
            AsyncThumbnailDownloader asyncThumbnailDownloader = new AsyncThumbnailDownloader(mainAct);
            asyncThumbnailDownloader.execute(temp.getString("strDrinkThumb"));

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mainAct,"Cocktail not found", Toast.LENGTH_SHORT).show();
        }

    }
}
