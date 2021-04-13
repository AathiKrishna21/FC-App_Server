package com.example.fcapp_server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fcapp_server.Model.Order;
import com.example.fcapp_server.ViewHolder.sOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference dRef,dbRef;
    Order order1;
    String shopId,name,phnno,orderid;

    FirebaseRecyclerAdapter<Order, sOrderViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database= FirebaseDatabase.getInstance();
        dRef= database.getReference("Orders");
        dbRef= database.getReference("PastOrders");

        recyclerView = findViewById(R.id.sorderrecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        shopId = getIntent().getStringExtra("ShopId");
        loadFoods(shopId);
    }

    private void loadFoods(String shopId) {
        FirebaseRecyclerOptions<Order> options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(dRef.orderByChild("shopId").equalTo(shopId),Order.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Order, sOrderViewHolder>(options) {

            @NonNull
            @Override
            public sOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sorder_items,parent,false);
                return new sOrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull sOrderViewHolder holder, final int position, @NonNull Order model) {
                holder.name.setText(model.getName());
                holder.phnno.setText(model.getPhonenumber());
                holder.orderId.setText(adapter.getRef(position).getKey());
                holder.status.setText(model.getStatus());
                holder.t_cost.setText(model.getTotalcost());

                holder.btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });
                holder.btndelivered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orderid=adapter.getRef(position).getKey();
                        dRef.child(orderid).child("status").setValue("3");
                        moveRecord(dRef.child(orderid),dbRef.child(orderid));


//                        dRef.child(orderid).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                order1=snapshot.getValue(Order.class);
//                                dbRef.child(orderid).setValue(order1);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                        deleteOrder(orderid);


                    }
                });


            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void deleteOrder(String key) {
        dRef.child(key).removeValue();
    }
    private void moveRecord(final DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            fromPath.removeValue();
                        } else {
//                                                Log.d(TAG, "Copy failed!");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }
}