package com.example.fcapp_server.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fcapp_server.R;
import com.squareup.picasso.Picasso;

public class MenuViewHolder extends RecyclerView.ViewHolder {
    public TextView name,cost;
    public ImageView photo;
    public Button edit;
    public  MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.food_name);
        photo=itemView.findViewById(R.id.imgpic);
        cost=itemView.findViewById(R.id.cost);
        edit=itemView.findViewById(R.id.btnadd);

    }
}