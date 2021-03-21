package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Loading categories
        AsyncCategoryData asyncCategoryData = new AsyncCategoryData(this);
        asyncCategoryData.execute("https://www.thecocktaildb.com/api/json/v1/1/list.php?c=list");

        //Search button
        Button searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.searchEditText)).getText().toString();

                Intent cocktailListIntent = new Intent(MainActivity.this, CocktailListActivity.class);
                cocktailListIntent.putExtra("cocktailName",name);
                startActivity(cocktailListIntent);
            }
        });

    }
}