package com.example.izobonga_waiting_app.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.izobonga_waiting_app.R;

public class GifActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        imageView = findViewById(R.id.image_view);
        Glide.with(this).load(R.raw.background_gif).into(imageView);


    }
}
