package com.example.carrenting.Service.Vehicle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.ActivityPages.OwnerMainActivity;
import com.example.carrenting.ActivityPages.ProfileActivity;
import com.example.carrenting.Model.User;
import com.example.carrenting.Model.Vehicle;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddVehicleActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private String documentId, downloadUrl;
    private Uri mImageURI;
    private EditText vehicle_name, vehicle_seats, vehicle_price, vehicle_owner, vehicle_number;
    private CheckBox vehicle_available;
    private Button btnAdd;
    private ImageView vehicle_imgView;
    private FirebaseFirestore dtb_vehicle, dtb_user;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String imageID;
    private User user = new User();
    private Vehicle vehicle = new Vehicle();

    ActivityResultLauncher<String> pickImagesFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent()
            , new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null){
                        mImageURI = result;
                        vehicle_imgView.setImageURI(result);
                    }
                    uploadImage();
                }
            });

    private void init() {
        vehicle_name = findViewById(R.id.et_name);
        vehicle_seats = findViewById(R.id.et_seats);
        vehicle_price = findViewById(R.id.et_price);
        vehicle_owner = findViewById(R.id.et_owner);
        vehicle_number = findViewById(R.id.et_number);
        vehicle_available = findViewById(R.id.cb_availability);
        vehicle_imgView = findViewById(R.id.img_view);
        btnAdd = findViewById(R.id.btn_add);

        dtb_vehicle = FirebaseFirestore.getInstance();
        dtb_user = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setUser_id(firebaseUser.getUid());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);


        init();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FullFill())
                {
                    addVehicle();
                }
                else
                {
                    Toast.makeText(AddVehicleActivity.this, "Vui lòng nhập đủ các thông tin", Toast.LENGTH_LONG).show();
                }
            }
        });

        vehicle_imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImagesFromGallery.launch("image/*");
            }
        });
    }

    private void addVehicle() {

        dtb_user.collection("Users")
            .whereEqualTo("user_id", user.getUser_id())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            vehicle.setProvider_id(document.get("user_id").toString());
                            vehicle.setProvider_name(document.get("username").toString());
                            vehicle.setProvider_address(document.get("address").toString() + " " + document.get("city").toString());
                            vehicle.setProvider_gmail(document.get("email").toString());
                            vehicle.setProvider_phone(document.get("phoneNumber").toString());

                            vehicle.setVehicle_name(vehicle_name.getText().toString());
                            vehicle.setVehicle_seats(vehicle_seats.getText().toString());
                            vehicle.setVehicle_price(vehicle_price.getText().toString() + " VND");
                            vehicle.setOwner_name(vehicle_owner.getText().toString());
                            vehicle.setVehicle_number(vehicle_number.getText().toString());
                            vehicle.setVehicle_availability("available");
                            vehicle.setVehicle_imageURL(downloadUrl);

                            dtb_vehicle.collection("Vehicles")
                                .add(vehicle)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        vehicle.setVehicle_id(documentReference.getId());

                                        Intent intent = new Intent(AddVehicleActivity.this, OwnerMainActivity.class);
                                        startActivity(intent);
                                        toast("Thêm xe thành công");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toast("Thêm xe thất bại");
                                    }
                                });
                        }
                    }
                    else {
                        //
                        Toast.makeText(AddVehicleActivity.this, "Không thể lấy thông tin", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    private boolean FullFill() {
        String[] arr = new String[]{vehicle_name.getText().toString(), vehicle_owner.getText().toString(),
                vehicle_number.getText().toString(), vehicle_price.getText().toString(), vehicle_seats.getText().toString()};
        for (String s : arr) {
            if (s.isEmpty()) {
                toast("Vui lòng nhập đầy đủ thông tin");
                return false;
            }
        }
        return true;
    }

    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }

    private void uploadImage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(mImageURI != null)
        {
            imageID = UUID.randomUUID().toString();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("VehicleImages/"+ imageID);
            ref.putFile(mImageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddVehicleActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = uri.toString();
                                    //Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddVehicleActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }
}