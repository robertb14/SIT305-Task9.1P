//Robert Bajan 17/05/22
package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lostandfound.data.DatabaseHelper;
import com.example.lostandfound.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity {

    ListView itemListView;

    ArrayList<String> itemArrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        itemListView = findViewById(R.id.itemListView);
        itemArrayList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(ItemList.this);

        List<Item> itemList = db.fetchAllItems();
        for (Item item : itemList) {
            itemArrayList.add(item.getType() + " " + item.getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemArrayList);
        itemListView.setAdapter(adapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ItemList.this, Info.class);
                intent.putExtra("id", itemList.get(i).getItem_id());
                intent.putExtra("type", itemList.get(i).getType());
                intent.putExtra("name", itemList.get(i).getName());
                intent.putExtra("phone", itemList.get(i).getPhone());
                intent.putExtra("desc", itemList.get(i).getDesc());
                intent.putExtra("date", itemList.get(i).getDate());
                intent.putExtra("location", itemList.get(i).getLocation());

                startActivity(intent);
            }
        });
    }
}