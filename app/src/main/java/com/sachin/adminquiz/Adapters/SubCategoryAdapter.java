package com.sachin.adminquiz.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.sachin.adminquiz.Models.SubCategoryModel;
import com.sachin.adminquiz.QuestionsActivity;
import com.sachin.adminquiz.R;
import com.sachin.adminquiz.databinding.RvSubcategoryDesignBinding;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.viewHolder> {

    Context context;
    ArrayList<SubCategoryModel>list;
    private String catId;
    private String subCatId;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModel> list, String catId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_subcategory_design,parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        SubCategoryModel categoryModel = list.get(position);

        holder.binding.subCategoryName.setText(categoryModel.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionsActivity.class);
                intent.putExtra("catId",catId);
                intent.putExtra("subCatId",categoryModel.getKey());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are You Sure, You Want To Delete This Category");

                builder.setPositiveButton("Yes",(dialogInterface, i) -> {

                    FirebaseDatabase.getInstance().getReference().child("categories").child(catId).child("subCategories").child(categoryModel.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                builder.setNegativeButton("No",(dialogInterface, i) -> {

                    dialogInterface.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return true;
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        RvSubcategoryDesignBinding binding;
        public viewHolder(@NonNull View itemView){
            super(itemView);

            binding = RvSubcategoryDesignBinding.bind(itemView);
        }
    }
}
