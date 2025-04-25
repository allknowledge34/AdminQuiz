package com.sachin.adminquiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.sachin.adminquiz.Adapters.CategoryAdapter;
import com.sachin.adminquiz.Models.CategoryModel;
import com.sachin.adminquiz.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CategoryAdapter adapter;
    ArrayList<CategoryModel>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setStatusBarColor();
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();


        binding.shimmerDialog.startShimmer();
        binding.shimmerDialog.setVisibility(View.VISIBLE);
        binding.rvCategory.setVisibility(View.GONE);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this, list);
        binding.rvCategory.setAdapter(adapter);


        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                        model.setKey(dataSnapshot.getKey());
                        list.add(model);
                    }

                    adapter.notifyDataSetChanged();
                    binding.shimmerDialog.stopShimmer();
                    binding.shimmerDialog.setVisibility(View.GONE);
                    binding.rvCategory.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "No categories found", Toast.LENGTH_SHORT).show();
                    binding.shimmerDialog.stopShimmer();
                    binding.shimmerDialog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.shimmerDialog.stopShimmer();
                binding.shimmerDialog.setVisibility(View.GONE);
            }
        });


        binding.uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        int statusBarColor = ContextCompat.getColor(this, R.color.lightblue);
        window.setStatusBarColor(statusBarColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}