package com.sena.senacomic.guide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sena.senacomic.databinding.ActivityGuideBinding;

public class GuideActivity extends AppCompatActivity {

    private ActivityGuideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}