//Robert Bajan 17/05/22
package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfound.data.DatabaseHelper;

public class Info extends AppCompatActivity {

    TextView txtViewType, txtViewName, txtViewDate, txtViewLocation, txtViewPhone;
    Button btnRemove;
    DatabaseHelper databaseHelper;

    private Integer itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        txtViewType = findViewById(R.id.txtViewType);
        txtViewName = findViewById(R.id.txtViewName);
        txtViewDate = findViewById(R.id.txtViewDate);
        txtViewLocation = findViewById(R.id.txtViewLocation);
        txtViewPhone = findViewById(R.id.txtViewPhone);

        btnRemove =findViewById(R.id.btnRemove);

        Intent intent = getIntent();
        itemID = intent.getIntExtra("id", -1);

        txtViewType.setText(intent.getStringExtra("type"));
        txtViewName.setText(intent.getStringExtra("name"));
        txtViewDate.setText(intent.getStringExtra("date"));
        txtViewLocation.setText(intent.getStringExtra("location"));
        txtViewPhone.setText(intent.getStringExtra("phone"));

        databaseHelper = new DatabaseHelper(this);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.onDelete(itemID);
                Toast.makeText(Info.this, "Removal Successful!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Info.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}