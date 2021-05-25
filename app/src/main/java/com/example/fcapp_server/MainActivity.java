package com.example.fcapp_server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Window window;
    String emailID,paswd,ShopId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        window=this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.status_bar));
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        final EditText emailId = findViewById(R.id.email);
        final EditText passwd = findViewById(R.id.editText3);
        final Button btnSignIn = findViewById(R.id.button2);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailID = emailId.getText().toString();
                paswd = passwd.getText().toString();
                if (emailID.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!(emailID.isEmpty() && paswd.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(emailID,paswd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "SignIn unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent=new Intent(MainActivity.this, Navigation1.class);
                                if(emailID.equals("barathnivas2011@gmail.com")) {
                                    ShopId = "01";
                                }
                                else if(emailID.equals("tanbumani1402@gmail.com")) {
                                    ShopId = "02";
                                }
                                else if(emailID.equals("raaghav2151@gmail.com")) {
                                    ShopId = "03";
                                }
                                else if(emailID.equals("aathikrishnask@gmail.com")) {
                                    ShopId = "04";
                                }
                                intent.putExtra("ShopId",ShopId);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }

        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        FirebaseUser user=firebaseAuth.getCurrentUser();
        Intent intent = new Intent(MainActivity.this,Navigation1.class);
        if (user != null) {
            String email=user.getEmail();
            if(email.equals("barathnivas2011@gmail.com")) {
                ShopId = "01";
            }
            else if(email.equals("tanbumani1402@gmail.com")) {
                ShopId = "02";
            }
            else if(email.equals("raaghav2151@gmail.com")) {
                ShopId = "03";
            }
            else if(email.equals("aathikrishnask@gmail.com")) {
                ShopId = "04";
            }
            intent.putExtra("ShopId",ShopId);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}
