package com.example.fcapp_server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fcapp_server.Model.Order;
import com.example.fcapp_server.ViewHolder.sOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    DatabaseReference dRef;

    String shopId,name,phnno;

    FirebaseRecyclerAdapter<Order, sOrderViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database= FirebaseDatabase.getInstance();
        dRef= database.getReference("Orders");
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


            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void deleteOrder(String key) {
        dRef.child(key).removeValue();
    }
}