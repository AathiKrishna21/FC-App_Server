package com.example.fcapp_server;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fcapp_server.Model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import javax.security.auth.callback.Callback;

import static android.app.Activity.RESULT_OK;

public class Add_item extends Fragment {
    EditText name,cost;
    Button add,choose;
    String fname,fcost;
    TextView fid;
    Switch sw;
    ImageView imageView;
    FirebaseStorage storage;
    DatabaseReference Ref;
    FirebaseDatabase database;
    StorageReference storageReference;
    String id,link;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_add_item, container, false);
        name=root.findViewById(R.id.fname);
        choose=root.findViewById(R.id.choose);
        cost=root.findViewById(R.id.fcost);
        add=root.findViewById(R.id.done);
        sw=root.findViewById(R.id.switch1);
        imageView=root.findViewById(R.id.photo);
        fid=root.findViewById(R.id.fid);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = storage.getReference();
        Ref= database.getReference("Foods");
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id= snapshot.child("count").getValue().toString();
                id=String.valueOf(Integer.parseInt(id)+1);
                fid.setText("Food Id : "+id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimg();
//                Toast.makeText(getContext(), "hiii", Toast.LENGTH_SHORT).show();

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname=name.getText().toString();
                fcost=cost.getText().toString();
                if(fname.isEmpty()){
                    name.setError("Provide your Dish Name!");
                    name.requestFocus();
                }else if (fcost.isEmpty()) {
                    cost.setError("Provide your Dish Price!");
                    cost.requestFocus();
                } else if (fname.isEmpty() && fcost.isEmpty()) {
                    Toast.makeText(getContext(), "Fields Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!fname.isEmpty() && !fcost.isEmpty()) {
//                    Toast.makeText(getContext(), "Done bro", Toast.LENGTH_SHORT).show();
                    uploadimg();

                }
            }
        });
        return root;
    }
    public void chooseimg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadimg() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            getid(new MyCallback() {
                @Override
                public void onCallback(String id) {
                    final StorageReference ref = storageReference.child("images/"+ id);
                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                    getid(new MyCallback() {
                                        @Override
                                        public void onCallback(final String id) {
                                            StorageReference ref = storageReference.child("images/"+ id);
                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
//                                    Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                                                    link=uri.toString();
                                                    Boolean switchState = sw.isChecked();
//                                    name.setText(link);
//                                    Toast.makeText(getContext(), "Uploaded "+link, Toast.LENGTH_SHORT).show();
                                                    Food f= new Food(link,"01",fname,fcost,switchState);
                                                    Ref.child(id).setValue(f);
                                                    Ref.child("count").setValue(id);
                                                }
                                            });
                                        }
                                    });
//                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
////                                    Log.d(TAG, "onSuccess: uri= "+ uri.toString());
//                                            link=uri.toString();
////                                            cost.setText(link);
//
//                                        }
//                                    });

//                            String downloadUrl = taskSnapshot.getMetadata().;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }
            });

        }
    }
    public interface MyCallback{
        void onCallback(String id);
    }
    public void getid(@NotNull final MyCallback callback) {

//        final Query query = mDatabase.child(FirebaseConstants.TARGET);

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = dataSnapshot.child("count").getValue().toString();
                id=String.valueOf(Integer.parseInt(id)+1);
                callback.onCallback(id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}