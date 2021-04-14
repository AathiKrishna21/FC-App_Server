package com.example.fcapp_server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fcapp_server.Model.Cart;
import com.example.fcapp_server.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Details extends Activity {
//    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    FirebaseDatabase database;
    DatabaseReference dRef,dbRef;
    List<Cart> cart;
    Order order1;
    TextView hi;
    RecyclerView detialsrcview;
    DetailsAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        DisplayMetrics dm=new DisplayMetrics();
        database= FirebaseDatabase.getInstance();
        dRef= database.getReference("Orders");
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        mProgressBar = new ProgressDialog(this);
//        hi=findViewById(R.id.hi);

//<!--            android:text="price"-->
//<!--            android:textSize="16dp"-->
//        mTableLayout = (TableLayout) findViewById(R.id.table);
//        mTableLayout.setStretchAllColumns(true);
        detialsrcview=findViewById(R.id.detailsrcview);
        detialsrcview.setHasFixedSize(true);
        detialsrcview.setLayoutManager(new LinearLayoutManager(this));
        startLoadData();
    }
    public void startLoadData() {
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage("Fetching Orders..");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
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
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Bundle extras = getIntent().getExtras();
//        Cart[] target=new Cart[];
        String id = extras.getString("id");
        dRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order1 = snapshot.getValue(Order.class);
                cart = order1.getFoods();
                adapter=new DetailsAdapter(Details.this,cart);
                detialsrcview.setAdapter(adapter);
//                hi.setText(order1.getName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            mProgressBar.hide();
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
