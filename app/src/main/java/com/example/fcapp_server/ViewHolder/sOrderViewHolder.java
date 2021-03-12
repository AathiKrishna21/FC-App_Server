package com.example.fcapp_server.ViewHolder;

import android.view.View;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fcapp_server.Interface.ItemClickListener;
import com.example.fcapp_server.R;


public class sOrderViewHolder extends RecyclerView.ViewHolder{
    public TextView orderId,name,phnno,status,t_cost;
    public Button btnstatus,btncancel,btndetails,btndelivered;
    private ItemClickListener itemCLickListener;



    public sOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        name=itemView.findViewById(R.id.name);
        phnno=itemView.findViewById(R.id.phnnum);
        status=itemView.findViewById(R.id.status);
        orderId=itemView.findViewById(R.id.orderid);
        t_cost=itemView.findViewById(R.id.total);
        btnstatus=itemView.findViewById(R.id.btnstatus);
        btncancel=itemView.findViewById(R.id.btncancel);
        btndetails=itemView.findViewById(R.id.btnorderdetail);
        btndelivered=itemView.findViewById(R.id.btndelivered);
    }
}