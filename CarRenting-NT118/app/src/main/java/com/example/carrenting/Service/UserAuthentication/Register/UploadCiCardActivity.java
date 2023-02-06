package com.example.carrenting.Service.UserAuthentication.Register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadCiCardActivity extends AppCompatActivity {

    private Button btnUpload;
    private ImageButton imgBtnNext;
    private ImageView vCardFront, vCardBehind;
    private ImageView illCardFront, illCardBehind;
    private StorageReference storageReference_front, storageReference_behind;
    Uri frontUri, behindUri;

    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ci_card);
        mDb = FirebaseFirestore.getInstance();
        storageReference_front = FirebaseStorage.getInstance().getReference("Citizen Card Front");
        storageReference_behind = FirebaseStorage.getInstance().getReference("Citizen Card Behind");

        btnUpload = findViewById(R.id.btnUpload);
        imgBtnNext = findViewById(R.id.imgBtnNext);

        illCardFront = findViewById(R.id.illCardFront);
        illCardBehind = findViewById(R.id.illCardBehind);

        vCardFront = findViewById(R.id.vCardFront);
        vCardBehind = findViewById(R.id.vCardBehind);

        vCardFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUpload.setVisibility(View.VISIBLE);
                ImagePicker.with(UploadCiCardActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(100);
            }
        });

        vCardBehind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUpload.setVisibility(View.VISIBLE);
                ImagePicker.Companion.with(UploadCiCardActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserDatabase();
            }
        });

        imgBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
    }

    private void nextActivity() {
        Intent intent = new Intent(UploadCiCardActivity.this, CustomerMainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void UpdateUserDatabase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        mDb.setFirestoreSettings(settings);
        DocumentReference newUserRef = mDb
                .collection(getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().getUid());

        StorageReference fileReference_front = storageReference_front.child(System.currentTimeMillis()
                + "." + getFileExtension(frontUri));
        fileReference_front.putFile(frontUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("URI",uri.toString());
                                newUserRef.update("ciCardFront", uri.toString());
                                //Toast.makeText(SignProfileActivity.this,"Upload image successful", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(SignProfileActivity.this, "Upload image failed", Toast.LENGTH_SHORT).show();
                    }
                });


        StorageReference fileReference_behind = storageReference_behind.child(System.currentTimeMillis()
                + "." + getFileExtension(behindUri));
        fileReference_behind.putFile(frontUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("URI",uri.toString());
                                newUserRef.update("ciCardBehind", uri.toString());
                                //Toast.makeText(SignProfileActivity.this,"Upload image successful", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(SignProfileActivity.this, "Upload image failed", Toast.LENGTH_SHORT).show();
                    }
                });
        Toast.makeText(UploadCiCardActivity.this,"Đăng tải hình căn cước công dân thành công", Toast.LENGTH_SHORT).show();
        btnUpload.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            frontUri = data.getData();

            vCardFront.setImageURI(frontUri);
            //Toast.makeText(UploadCiCardActivity.this, "Success", Toast.LENGTH_SHORT).show();
            illCardFront.setVisibility(View.GONE);
        }
        else if (requestCode == 101 && resultCode == Activity.RESULT_OK)
            {
                behindUri = data.getData();

                vCardBehind.setImageURI(behindUri);
                ///Toast.makeText(UploadCiCardActivity.this,"Success", Toast.LENGTH_SHORT).show();
                illCardBehind.setVisibility(View.GONE);
            }
    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}