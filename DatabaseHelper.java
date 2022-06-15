//Robert Bajan 17/05/22
package com.example.lostandfound.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;

import com.example.lostandfound.model.Item;
import com.example.lostandfound.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + Util.TYPE + " TEXT," + Util.NAME + " TEXT," + Util.PHONE + " TEXT," + Util.DESCRIPTION + " TEXT," + Util.DATE + " TEXT,"
                + Util.LOCATION + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    public void onDelete(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " DELETE FROM " + Util.TABLE_NAME + " WHERE " + Util.ITEM_ID + " = '" + id + "'";
        int result = db.delete(Util.TABLE_NAME, Util.ITEM_ID+"=?", new String[]{id.toString()});
        Log.d("delete", "deleteName: result: " + result);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS";
        sqLiteDatabase.execSQL(DROP_ITEM_TABLE, new String[]{Util.TABLE_NAME});

        onCreate(sqLiteDatabase);
    }

    public long insertItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.TYPE, item.getType());
        contentValues.put(Util.NAME, item.getName());
        contentValues.put(Util.PHONE, item.getPhone());
        contentValues.put(Util.DESCRIPTION, item.getDesc());
        contentValues.put(Util.DATE, item.getDate());
        contentValues.put(Util.LOCATION, item.getLocation());

        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();

        return newRowId;
    }

    public Cursor getItem(){
         SQLiteDatabase db = this.getWritableDatabase();
         String getItem="SELECT * FROM "+ Util.TABLE_NAME;
         Cursor data=db.rawQuery(getItem,null);
         return data;
         }

    public List<Item> fetchAllItems(){
        List<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = " SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if(cursor.moveToFirst()){
            do {
                Item item = new Item();
                item.setItem_id(cursor.getInt(0));
                item.setType(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setPhone(cursor.getString(3));
                item.setDesc(cursor.getString(4));
                item.setDate(cursor.getString(5));
                item.setLocation(cursor.getString(6));

                itemList.add(item);
            }while (cursor.moveToNext());
        }
        return itemList;
    }
}
