package com.example.carrenting.Service.Vehicle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.carrenting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddVehicleActivity extends AppCompatActivity {

    private String documentId, path;
    private Uri mImageURI, downloadUri;
    private EditText vehicle_name, vehicle_seats, vehicle_price, vehicle_owner, vehicle_number;
    private CheckBox vehicle_available;
    private Button btnAdd;
    private ImageView vehicle_imgView;
    private FirebaseFirestore dtb_vehicle;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DocumentReference documentRef;

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

    private void findViewByIds() {
        vehicle_name = findViewById(R.id.et_name);
        vehicle_seats = findViewById(R.id.et_seats);
        vehicle_price = findViewById(R.id.et_price);
        vehicle_owner = findViewById(R.id.et_owner);
        vehicle_number = findViewById(R.id.et_number);
        vehicle_available = findViewById(R.id.cb_availability);
        vehicle_imgView = findViewById(R.id.img_view);
        btnAdd = findViewById(R.id.btn_add);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        findViewByIds();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FullFill() == true)
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
        dtb_vehicle = FirebaseFirestore.getInstance();

        String availability = vehicle_available.isChecked() ? "available" : "unavailable";

        Map<String, Object> vehicle = new HashMap<>();
        vehicle.put("name", vehicle_name.getText().toString());
        vehicle.put("seats", vehicle_seats.getText().toString());
        vehicle.put("price", vehicle_price.getText().toString());
        vehicle.put("owner", vehicle_owner.getText().toString());
        vehicle.put("number", vehicle_number.getText().toString());
        /*        vehicle.put("imageURL", "null");*/
        vehicle.put("availability", availability);
        dtb_vehicle.collection("Vehicle")
                .add(vehicle)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentId = documentReference.getId();
                        documentRef = dtb_vehicle.document("Vehicle/" + documentId);
                        Toast.makeText(AddVehicleActivity.this, "Vehicle added successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error adding document", e);
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
        // Check if an image was selected
        if (vehicle_imgView.getDrawable() == null) {
            Toast.makeText(AddVehicleActivity.this, "Please select an image", Toast.LENGTH_LONG).show();
            return;
        }

        // Get image from ImageView as bitmap
        Bitmap bitmap = ((BitmapDrawable) vehicle_imgView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Initialize storage reference
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Create a reference to "VehicleImages/documentId.jpg"
        StorageReference imageRef = storageRef.child("VehicleImages/" + documentId + ".jpg");

        // Upload image
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w("Error uploading image", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the public URL of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUri = uri;
                        documentRef.update("imageURL", downloadUri.toString());
                    }
                });

                Toast.makeText(AddVehicleActivity.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

}