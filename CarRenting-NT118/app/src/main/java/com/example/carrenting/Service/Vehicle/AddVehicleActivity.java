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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddVehicleActivity extends AppCompatActivity {

    private String documentId, downloadUrl;
    private Uri mImageURI;
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
        vehicle.put("vehicle_name", vehicle_name.getText().toString());
        vehicle.put("seats", vehicle_seats.getText().toString());
        vehicle.put("vehicle_price", vehicle_price.getText().toString());
        vehicle.put("owner_name", vehicle_owner.getText().toString());
        vehicle.put("plate_number", vehicle_number.getText().toString());
        vehicle.put("availability", availability);
        vehicle.put("imageURL", "");

        dtb_vehicle.collection("Vehicles")
                .add(vehicle)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        DocumentReference documentRef = documentReference;
                        documentId = documentReference.getId();
                        uploadImage();

                        DocumentReference updating = dtb_vehicle.collection("Vehicles").document(documentId);
                        updating
                                .update("imageURL",downloadUrl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                       toast("Thêm xe thành công");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toast("Thêm xe thất bại");
                                        dtb_vehicle.collection("Vehicles").document(documentId).delete();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toast("Thêm xe thất bại");
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
            Toast.makeText(AddVehicleActivity.this, "Hãy chọn một hình ảnh", Toast.LENGTH_LONG).show();
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
            public void onFailure(@NonNull Exception e) {
                toast("Không thể tải ảnh lên");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                toast("Tải ảnh lên thành công");
            }
        });
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadUrl = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                toast("Tải lên thất bại");
            }
        });

    }
}