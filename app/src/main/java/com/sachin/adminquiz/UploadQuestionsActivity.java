package com.sachin.adminquiz;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.sachin.adminquiz.Models.QuestionsModel;
import com.sachin.adminquiz.databinding.ActivityUploadQuestionsBinding;

import java.util.ArrayList;

public class UploadQuestionsActivity extends AppCompatActivity {

    ActivityUploadQuestionsBinding binding;
    FirebaseDatabase database;
    RadioGroup options;
    LinearLayout answers;
    private String catId,subCatId;
    Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        catId = getIntent().getStringExtra("catId");
        subCatId = getIntent().getStringExtra("subCatId");

        database = FirebaseDatabase.getInstance();

        options = findViewById(R.id.options);
        answers = findViewById(R.id.answers);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int correct = -1;

                for (int i=0;i<options.getChildCount();i++){

                    EditText answ = (EditText) answers.getChildAt(i);

                    if (answ.getText().toString().isEmpty()){

                        answ.setError("Required");
                        return;
                    }

                    RadioButton radioButton = (RadioButton) options.getChildAt(i);

                    if (radioButton.isChecked()){
                        correct=i;
                        break;
                    }
                }

                if (correct==-1){

                    Toast.makeText(UploadQuestionsActivity.this,"Select Correct Option", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingDialog.show();
                QuestionsModel model = new QuestionsModel();
                model.setQuestion(binding.edtQuestion.getText().toString());
                model.setOptionA(((EditText)answers.getChildAt(0)).getText().toString());
                model.setOptionB(((EditText)answers.getChildAt(1)).getText().toString());
                model.setOptionC(((EditText)answers.getChildAt(2)).getText().toString());
                model.setOptionD(((EditText)answers.getChildAt(3)).getText().toString());
                model.setCorrectAnswer (((EditText) answers.getChildAt(correct)).getText().toString());

                database.getReference().child("categories").child(catId).child("subCategories").child(subCatId).child("questions").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        loadingDialog.dismiss();
                        Toast.makeText(UploadQuestionsActivity.this,"questions uploaded", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(UploadQuestionsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}