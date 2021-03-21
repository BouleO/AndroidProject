package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CocktailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktail);

        Intent cocktailIntent = getIntent();
        String id = cocktailIntent.getStringExtra("cocktailID");

        AsyncCocktailSearchById asyncCocktailSearchById = new AsyncCocktailSearchById(this);
        String query = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + id;
        Log.i("drinkID",id);
        asyncCocktailSearchById.execute(query);

    }
}