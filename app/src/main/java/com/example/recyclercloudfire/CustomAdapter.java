package com.example.recyclercloudfire;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


    private ArrayList<User> users = new ArrayList<>();
    private Context context;
    private FirebaseFirestore fs;

    public CustomAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myrow, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.id_tv.setText(String.valueOf(user.getId()));
        holder.name_tv.setText(user.getName());
        holder.email_tv.setText(user.getEmail());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("id", user.getId());
                context.startActivity(intent);
            }
        });

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fs = FirebaseFirestore.getInstance();
                        Query query = fs.collection("users").whereEqualTo("id", user.getId());
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        documentSnapshot.getReference().delete();
                                    }


                                }

                            }
                        });



                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id_tv, name_tv, email_tv;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id_tv = itemView.findViewById(R.id.id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            email_tv = itemView.findViewById(R.id.email_tv);

            parent = itemView.findViewById(R.id.parent);

        }
    }
}
