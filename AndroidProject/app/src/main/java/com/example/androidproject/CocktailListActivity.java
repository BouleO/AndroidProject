package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class CocktailListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktaillist);

        //Getting the parameters from the intent
        Intent cocktailListIntent = getIntent();
        String category = cocktailListIntent.getStringExtra("category");
        String name = cocktailListIntent.getStringExtra("cocktailName");

        //if the intent comes from the categories
        if(category!=null){
            AsyncCocktailListFromCategory asyncCocktailListFromCategory = new AsyncCocktailListFromCategory(this);
            String query = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=" + category;
            asyncCocktailListFromCategory.execute(query);
        }

        //if the intent comes from the individual search
        else if(name!=null){
            AsyncCocktailSearch asyncCocktailSearch = new AsyncCocktailSearch(this);
            String query = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + name;
            asyncCocktailSearch.execute(query);
        }




    }
}