package com.example.carrenting.Service.UserAuthentication.Register;


import static com.example.carrenting.ActivityPages.CustomerMainActivity.AVATAR_REQUEST_CODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.Model.User;
import com.example.carrenting.R;
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
import java.util.HashMap;
import java.util.Map;

public class SignProfileActivity extends AppCompatActivity {
    private TextView tvBirthDate;
    private ImageView imgAvatar;
    private Uri mUri;
    private Button btnUpdate;
    private FirebaseFirestore mDb;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_profile);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        imgAvatar = findViewById(R.id.img_avatar_profile);
        storageReference = FirebaseStorage.getInstance().getReference("User");


        mDb = FirebaseFirestore.getInstance();
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        tvBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar(tvBirthDate);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }


    private void updateUser() {

        String strUserName = ((EditText)findViewById(R.id.edtUserName)).getText().toString();

        String driverLicense = ((EditText)findViewById(R.id.edtLicense)).getText().toString();
        String dateOfBirth = tvBirthDate.getText().toString();

        String strStreet = ((EditText)findViewById(R.id.edtStreet)).getText().toString();
        String strCity = ((EditText)findViewById(R.id.edtCity)).getText().toString();
        String strPostalCode = ((EditText)findViewById(R.id.edtPostalCode)).getText().toString();

        Map<String, Object> dataUser = new HashMap<>();
        dataUser.put("username", strUserName);
        dataUser.put("driverLicense", driverLicense);
        dataUser.put("dateOfBirth", dateOfBirth);
        dataUser.put("street", strStreet);
        dataUser.put("city", strCity);
        dataUser.put("postalCode", strPostalCode);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        mDb.setFirestoreSettings(settings);
        DocumentReference newUserRef = mDb
                .collection(getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().getUid());

        newUserRef.update(dataUser);
        Toast.makeText(SignProfileActivity.this,"Update user information successful", Toast.LENGTH_SHORT).show();

        if (mUri != null)
        {
            progressDialog = new ProgressDialog(this);
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mUri));
            fileReference.putFile(mUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("URI",uri.toString());
                            newUserRef.update("avatarURL", uri.toString());

                        }
                    });
                }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignProfileActivity.this, "Upload image failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(SignProfileActivity.this, UploadCiCardActivity.class);
                            startActivity(intent);

                        }
                    });
            Toast.makeText(SignProfileActivity.this,"Upload image successful", Toast.LENGTH_LONG).show();
        }
    }

    private void openCalendar(final TextView dateFieldButton) {
        DatePickerDialog datePickerDialog = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog = new DatePickerDialog(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = year + "-" + month + "-" + dayOfMonth;
                    dateFieldButton.setText(date);
                }
            });
        }

        datePickerDialog.show();
    }
    private void onClickRequestPermission()
    {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            this.openGallery();
            return;
        }
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            this.openGallery();
        }
        else
        {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            this.requestPermissions(permission, AVATAR_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AVATAR_REQUEST_CODE)
        {
            if (grantResults.length >  0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openGallery();
            }

        }
    }
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK)
                    {
                        Intent intent = result.getData();
                        if (intent == null)
                        {
                            return;
                        }
                        mUri = intent.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                            imgAvatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}