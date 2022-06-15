//Robert Bajan 17/05/22
package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnCreate, btnShow, btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = findViewById(R.id.btnCreate);
        btnShow = findViewById(R.id.btnShow);
        btnMap = findViewById(R.id.btnMap);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAdvert();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityShow();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLocationMap();
            }
        });
    }

    public void openActivityAdvert(){
        Intent intent = new Intent(this, NewAdvert.class);
        startActivity(intent);
    }
    public void openActivityShow(){
        Intent intent = new Intent(this, ItemList.class);
        startActivity(intent);
    }
    public void openActivityLocationMap(){
        Intent intent = new Intent(this, LocationMap.class);
        startActivity(intent);
    }
}