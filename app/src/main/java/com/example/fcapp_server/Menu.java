package com.example.fcapp_server;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Menu extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Cart> listData = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference dRef,Ref;
    ImageView img,back;
    TextView shop_name;
    FirebaseRecyclerAdapter<Food, MenuViewHolder> adapter;
    Window window;
    String shopId,FoodId,orderid;
    List<Cart> cart;
    Cart cart1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_menu, container, false);
        database= FirebaseDatabase.getInstance();
        dRef= database.getReference("Foods");
        Ref = database.getReference("Shops");
        recyclerView = root.findViewById(R.id.menurecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
//        window=this.getWindow();
//        window.setStatusBarColor(this.getResources().getColor(R.color.status_bar));
//        shopId = getIntent().getStringExtra("ShopId");
        shopId="01";
        loadFoods("01");
        return root;
    }
    private void loadFoods(final String shopId) {
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(dRef.orderByChild("MenuId").equalTo(shopId),Food.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Food, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position, @NonNull final Food model) {
                holder.cost.setText("Rs."+model.getPrice());
                holder.name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.photo);
                String id=adapter.getRef(position).getKey();
                holder.edit.setOnClickListener(new EditOnClickListener(id,getActivity()));
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
    public class EditOnClickListener implements View.OnClickListener
    {

        String id;
        Context context;
        public EditOnClickListener(String id,Context context) {
            this.id = id;
            this.context=context;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DishEdit.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        //read your lovely variable
    }
}