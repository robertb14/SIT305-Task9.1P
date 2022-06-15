//Robert Bajan 17/05/22
package com.example.lostandfound;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lostandfound.data.DatabaseHelper;
import com.example.lostandfound.model.Item;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NewAdvert extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText editTxtName, editTxtPhone, editTxtDesc, editTxtDate, editTxtLocation;
    Button btnSave, btnCurrent;
    DatabaseHelper db;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advert);

        radioGroup = findViewById(R.id.radioGroup);

        editTxtName = findViewById(R.id.editTxtName);
        editTxtPhone = findViewById(R.id.editTxtPhone);
        editTxtDesc = findViewById(R.id.editTxtDesc);
        editTxtDate = findViewById(R.id.editTxtDate);
        editTxtLocation = findViewById(R.id.editTxtLocation);

        btnSave = findViewById(R.id.btnSave);
        btnCurrent = findViewById(R.id.btnCurrent);

        db = new DatabaseHelper(this);

        Places.initialize(getApplicationContext(), "AIzaSyBUMbS1Mx8qWNLnYMkur0Q5BjwK_FFsZhk");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);

                String type = radioButton.getText().toString();
                String name = editTxtName.getText().toString();
                String phone = editTxtPhone.getText().toString();
                String desc = editTxtDesc.getText().toString();
                String date = editTxtDate.getText().toString();
                String location = editTxtLocation.getText().toString();

                long result = db.insertItem(new Item(type, name, phone, desc, date, location));

                if(result > 0){
                    Toast.makeText(NewAdvert.this, "Successful Post", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(NewAdvert.this, ItemList.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(NewAdvert.this, "Post Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result != null && result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        editTxtLocation.setText(String.valueOf(place.getLatLng().latitude) + "," + String.valueOf(place.getLatLng().longitude));
                    }
                }
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        editTxtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(NewAdvert.this);

                resultLauncher.launch(intent);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(NewAdvert.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getCurrentLoc();
                }
                else{
                    Toast.makeText(NewAdvert.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(NewAdvert.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

         if(resultCode == RESULT_OK){
             Place place =Autocomplete.getPlaceFromIntent(data);
             editTxtLocation.setText(String.valueOf(place.getLatLng().latitude)+","+ String.valueOf(place.getLatLng().longitude));
         }
         else if(resultCode == AutocompleteActivity.RESULT_ERROR){
             Toast.makeText(NewAdvert.this, "Autocomplete Failed!", Toast.LENGTH_SHORT).show();
             Status status = Autocomplete.getStatusFromIntent(data);
         }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCurrentLoc(){
        if(ActivityCompat.checkSelfPermission(NewAdvert.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewAdvert.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Geocoder geocoder = new Geocoder(NewAdvert.this, Locale.getDefault());

                try {
                    List<Address> point = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String loc = String.valueOf(point.get(0).getLatitude()) + "," + String.valueOf(point.get(0).getLongitude());

                    editTxtLocation.setText(loc);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();

                if(location != null){
                    Toast.makeText(NewAdvert.this, "Current Update Location Success!", Toast.LENGTH_SHORT).show();
                }

                try {
                    Geocoder geocoder = new Geocoder(NewAdvert.this, Locale.getDefault());
                    List<Address> pointList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    String temp = String.valueOf(pointList.get(0).getLatitude()) + "," + String.valueOf(pointList.get(0).getLongitude());

                    editTxtLocation.setText(temp);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}