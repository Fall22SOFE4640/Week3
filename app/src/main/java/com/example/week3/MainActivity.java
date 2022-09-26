package com.example.week3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
   Spinner spCities;
   RadioGroup grUni;
   ProgressBar progressBar;
   Button btnIntent;

   private static final int CAMERA_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=10;i<300;i=+10) {
                    progressBar.incrementProgressBy(i);
                    SystemClock.sleep(500);
                }
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        thread.start();

        grUni= findViewById(R.id.rgUni);

        grUni.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.otu:
                        Toast.makeText(MainActivity.this,"You select: OTU",Toast.LENGTH_LONG).show();
                         break;
                    case R.id.uft:
                        Toast.makeText(MainActivity.this,"You select: uft",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.ou:
                        Toast.makeText(MainActivity.this,"You select: ou",Toast.LENGTH_LONG).show();
                    default:
                        break;
                }

            }
        });
        spCities = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.cities, android.R.layout.simple_spinner_dropdown_item);

        spCities.setAdapter(adapter);
        spCities.setOnItemSelectedListener(this);

        ActivityResultLauncher<Intent>  startForResult=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() ==RESULT_OK && result.getData()!=null){
                    Bundle bundle = result.getData().getExtras();
                    if (bundle!=null){
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        Glide.with(MainActivity.this)
                                .asBitmap()
                                .load(bitmap)
                                .into(imageView);


                    }
                }
            }
        });
        btnIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startForResult.launch(intent);

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String city = spCities.getItemAtPosition(position).toString();
        Toast.makeText(this,"You select: " + city,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}