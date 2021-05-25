package com.example.fcapp_server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fcapp_server.Model.Cart;
import com.example.fcapp_server.Model.Food;
import com.example.fcapp_server.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Menu_trial extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Cart> listData = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference dRef,Ref;
    ImageView img,back;
    TextView shop_name;
    FirebaseRecyclerAdapter<Food, MenuViewHolder> adapter;
    Window window;
    String shopId,FoodId;
    List<Cart> cart;
    Cart cart1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);
//        img=findViewById(R.id.shop_img);
//        shop_name=findViewById(R.id.shopname);
//        back=findViewById(R.id.back_button);
        database= FirebaseDatabase.getInstance();
        dRef= database.getReference("Foods");
        Ref = database.getReference("Shops");
        recyclerView = findViewById(R.id.menurecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        window=this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.status_bar));
//        shopId = getIntent().getStringExtra("ShopId");
        shopId="01";
        loadFoods("01");
    }

    private void loadFoods(final String shopId) {
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(dRef.orderByChild("MenuId").equalTo(shopId),Food.class)
                        .build();
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Menu_trial.this,Main2Activity.class);
//                startActivity(intent);
//            }
//        });
        Ref.child(shopId).child("Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url=snapshot.getValue(String.class);
                Picasso.get().load(url).into(img);
                shop_name.setText(convertCodetoShop(shopId));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new FirebaseRecyclerAdapter<Food, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position, @NonNull final Food model) {
                holder.cost.setText("Rs."+model.getPrice());
                holder.name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.photo);
//                if(!cart.isEmpty()) {
//                    cart1=cart.get(0);
//                    for(Cart c : cart) {
//                        if (c.getProductId().equals(adapter.getRef(position).getKey())) {
//                            holder.cost.setText(adapter.getRef(position).getKey());
//                            holder.add.setVisibility(View.GONE);
//                            holder.quantity.setVisibility(View.VISIBLE);
//                            holder.quantity.setNumber(c.getQuantity());
//                        }
//                    }
//                    FoodId = cart1.getProductId();
//                    dRef.child(FoodId).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            String ShopId = snapshot.child("MenuId").getValue().toString();
//                            if(!shopId.equals(ShopId)){
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//                else{
//                    Picasso.get().load(model.getImage()).into(holder.photo);
//                }




//                holder.add.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        new Database(getBaseContext()).addToCart(new Cart(adapter.getRef(position).getKey(),model.getName(),"1",model.getPrice()));
//                        holder.quantity.setVisibility(View.VISIBLE);
//                        holder.add.setVisibility(View.GONE);
//                    }
//
//                });



            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish,parent,false);
                return new MenuViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Menu_trial.this,Orders.class);
        startActivity(intent);
    }
    private String convertCodetoShop(String menuId) {
        if(menuId.equals("01"))
            return "Lakshmi Bhavan";
        else if(menuId.equals("02"))
            return "Idly Italy";
        else if(menuId.equals("03"))
            return "Chat Shop";
        else if(menuId.equals("04"))
            return "Juice Shop";
        return "";
    }
    class LoadDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            loadFoods(shopId);

        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}
