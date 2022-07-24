package com.example.recyclercloudfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    private Button ok_btn, cancel_btn;
    private TextView zero_tv;
    private EditText name_et, email_et;
    private FirebaseFirestore db;
    private User user;
    private static int staticId = 1;
    private DocumentReference dRef;
    private int intentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name_et = findViewById(R.id.name_et);
        email_et = findViewById(R.id.email_rt);
        zero_tv = findViewById(R.id.zero_tv);

        ok_btn = findViewById(R.id.ok_btn);
        cancel_btn = findViewById(R.id.cancel_btn);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        intentID = intent.getIntExtra("id", -1);
        User[] newUser = {null};

        if (intentID > 0) {
            db.collection("users").whereEqualTo("id", intentID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            newUser[0] = document.toObject(User.class);
                            dRef = document.getReference();


                        }
                        name_et.setText(newUser[0].getName());
                        email_et.setText(newUser[0].getEmail());
                        zero_tv.setText(String.valueOf(intentID));
                    }
                }
            });
        }

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intentID > 0) {
                    String name = name_et.getText().toString();
                    String email = email_et.getText().toString();
                    User editUser = new User(intentID, name, email);


                    dRef.set(editUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    String name = name_et.getText().toString();
                    String email = email_et.getText().toString();
                    user = new User(staticId, name, email);

                    staticId++;


                    db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }


            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}