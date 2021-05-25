package com.example.fcapp_server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fcapp_server.Model.Cart;
import com.example.fcapp_server.Model.Food;
import com.example.fcapp_server.Model.Order;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DishEdit extends Activity {
    ProgressDialog mProgressBar;
    FirebaseDatabase database;
    DatabaseReference dRef,dbRef;
    String new_name,new_cost,new_img;
    EditText fname,fimg,fcost;
    TextView name,foodid,img,cost;
    Food food;
    Switch sw;
    Button done;
    RelativeLayout rl;
    SpinKitView threebounce;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_edit);
        DisplayMetrics dm=new DisplayMetrics();
        database= FirebaseDatabase.getInstance();
        dRef= database.getReference("Foods");
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
//        mProgressBar = new ProgressDialog(this);
        foodid=findViewById(R.id.foodid);
        fimg=findViewById(R.id.fimg);
        fname=findViewById(R.id.fname);
        fcost=findViewById(R.id.fcost);
        done=findViewById(R.id.done);
        sw=findViewById(R.id.switch1);
        rl=findViewById(R.id.relative);
        rl.setVisibility(View.GONE);
        threebounce = findViewById(R.id.spin_kit);
        Sprite tb = new ThreeBounce();
        threebounce.setIndeterminateDrawable(tb);
        startLoadData();
    }
    public void startLoadData() {
//        mProgressBar.setCancelable(false);
//        mProgressBar.setMessage("Fetching Orders..");
//        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressBar.show();
        threebounce.setVisibility(View.VISIBLE);
        new LoadDataTask().execute(0);
    }
    public void loadData() {
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize = 0, mediumTextSize = 0;
        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);
        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        foodid.setText("Food Id: "+id);
        dRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                food = snapshot.getValue(Food.class);
                fname.setText(food.getName());
                fcost.setText(food.getPrice());
                fimg.setText(food.getImage());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                // Set a title for alert dialog
                builder.setTitle("Edit Dish");
                // Ask the final question
                builder.setMessage("Are you sure to edit the dish permanently?");
                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new_cost=fcost.getText().toString();
                        new_img=fimg.getText().toString();
                        new_name=fname.getText().toString();
                        Boolean switchState = sw.isChecked();
                        dRef.child(id).child("Name").setValue(new_name);
                        dRef.child(id).child("Image").setValue(new_img);
                        dRef.child(id).child("Price").setValue(new_cost);
                        dRef.child(id).child("Available").setValue(switchState);
                        finish();
                    }
                });
                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
//                                    Toast.makeText(getApplicationContext(),
//                                            "No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();

            }
        });
//        new_cost=fcost.getText().toString();
//        new_img=fimg.getText().toString();
//        new_name=fname.getText().toString();
//        dRef.child(id).child("Name").setValue(new_name);
//        dRef.child(id).child("Image").setValue(new_img);
//        dRef.child(id).child("Price").setValue(new_cost);

//        cart.get(0).getProductName()
//        Cart[] a = cart.toArray(new Cart[cart.size()]);
//        adapter=new DetailsAdapter(this,cart);
//        detialsrcview.setAdapter(adapter);
//        int rows = data.length;
//        getSupportActionBar().setTitle("Invoices (" + String.valueOf(rows) + ")");
//        TextView textSpacer = null;
//        mTableLayout.removeAllViews();
        // -1 means heading row
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
            threebounce.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
            loadData();
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}
