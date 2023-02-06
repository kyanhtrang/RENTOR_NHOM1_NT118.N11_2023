package com.example.carrenting.FragmentPages.Customer.UserInfor;

import static android.content.ContentValues.TAG;

import static com.example.carrenting.ActivityPages.CustomerMainActivity.BEHIND_CICARD_REQUEST_CODE;
import static com.example.carrenting.ActivityPages.CustomerMainActivity.FRONT_CICARD_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.example.carrenting.Service.UserAuthentication.Register.RegisterActivity;
import com.example.carrenting.Service.UserAuthentication.Register.SignProfileActivity;
import com.example.carrenting.Service.UserAuthentication.Register.UploadCiCardActivity;
import com.example.carrenting.Service.UserAuthentication.Register.ValidatePhoneActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyProfileFragment extends Fragment {

    private View mView;
    private ImageView imgAvatar;
    private EditText edtPhone, edtEmail, edtUserName, edtLicense, edtStreet, edtCity, edtPostalCode;
    private TextView tvBirthDate;
    private ImageView ivFrontCiCard, ivBehindCiCard;
    private Button btnUpdate;
    private Uri mAvatarUri;
    private CustomerMainActivity mMainActivity;
    private ProgressDialog progressDialog;
    private FirebaseFirestore mDb;
    private StorageReference storageReference_front, storageReference_behind, storageReference_avatar;
    User mUser;
    private Uri frontUri, behindUri;
    public void setAvatarUri(Uri mUri) {
        this.mAvatarUri = mUri;
    }

    public void setFrontUri(Uri frontUri) {
        this.frontUri = frontUri;
    }

    public void setBehindUri(Uri behindUri) {
        this.behindUri = behindUri;
    }


    public int getFlag_image() {
        return flag_image;
    }

    public void setFlag_image(int flag_image) {
        this.flag_image = flag_image;
    }

    public ImageView getImgAvatar() {
        return imgAvatar;
    }

    public ImageView getIvFrontCiCard() {
        return ivFrontCiCard;
    }

    public ImageView getIvBehindCiCard() {
        return ivBehindCiCard;
    }

    private int flag_image = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initUI();
        mDb = FirebaseFirestore.getInstance();
        storageReference_front = FirebaseStorage.getInstance().getReference("Citizen Card Front");
        storageReference_behind = FirebaseStorage.getInstance().getReference("Citizen Card Behind");
        storageReference_avatar = FirebaseStorage.getInstance().getReference("User");
        mMainActivity = (CustomerMainActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());
        setUserInformation();
        initListener();


        return mView;
    }

    private void initListener()
    {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFlag_image(0);
                mMainActivity.onClickRequestPermission(CustomerMainActivity.AVATAR_REQUEST_CODE);
                Glide.with(getActivity()).load(mAvatarUri).into(imgAvatar);

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProfile();
            }
        });
        tvBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar(tvBirthDate);
            }
        });
        ivFrontCiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFlag_image(1);
                mMainActivity.onClickRequestPermission(FRONT_CICARD_REQUEST_CODE);
                Glide.with(getActivity()).load(frontUri).into(ivFrontCiCard);
            }
        });

        ivBehindCiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFlag_image(2);
                mMainActivity.onClickRequestPermission(BEHIND_CICARD_REQUEST_CODE);
                Glide.with(getActivity()).load(behindUri).into(ivBehindCiCard);
            }
        });

    }

    private void openCalendar(TextView tvBirthDate) {
        DatePickerDialog datePickerDialog = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog = new DatePickerDialog(getActivity());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = year + "-" + month + "-" + dayOfMonth;
                    tvBirthDate.setText(date);
                }
            });
        }

        datePickerDialog.show();
    }

    private void onClickUpdateProfile() {
        String strPhone = ((EditText) mView.findViewById(R.id.edtPhone)).getText().toString();
        String strEmail = ((EditText) mView.findViewById(R.id.edtEmail)).getText().toString();
        String strUserName = ((EditText) mView.findViewById(R.id.edtUserName)).getText().toString();
        String driverLicense = ((EditText) mView.findViewById(R.id.edtLicense)).getText().toString();
        String strStreet = ((EditText) mView.findViewById(R.id.edtStreet)).getText().toString();
        String strCity = ((EditText) mView.findViewById(R.id.edtCity)).getText().toString();
        String strPostalCode = ((EditText) mView.findViewById(R.id.edtPostalCode)).getText().toString();
        String dateOfBirth = ((TextView) mView.findViewById(R.id.tvBirthDate)).getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null)
        {
            return;
        }
        progressDialog.show();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strUserName)
                .setPhotoUri(mAvatarUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Upload profile successfull", Toast.LENGTH_LONG).show();
                            mMainActivity.showUserInformation();
                        }
                    }
                });

        user.updateEmail(strEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });

        Map<String, Object> dataUser = new HashMap<>();
        dataUser.put("email", strEmail);
        dataUser.put("phoneNumber", strPhone);
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
        Toast.makeText(getActivity(),"Update user information successful", Toast.LENGTH_SHORT).show();

        StorageReference fileReference_avatar = storageReference_avatar.child(System.currentTimeMillis()
                + "." + mMainActivity.getFileExtension(mAvatarUri));
        fileReference_avatar.putFile(mAvatarUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("URI",uri.toString());
                                newUserRef.update("avatarURL", uri.toString());
                                Toast.makeText(getActivity(),"Upload avatar successful", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getActivity(), "Upload image failed", Toast.LENGTH_SHORT).show();
                    }
                });

        StorageReference fileReference_front = storageReference_front.child(System.currentTimeMillis()
                + "." + mMainActivity.getFileExtension(frontUri));
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
                        //Toast.makeText(getActivity(), "Upload image failed", Toast.LENGTH_SHORT).show();
                    }
                });


        StorageReference fileReference_behind = storageReference_behind.child(System.currentTimeMillis()
                + "." + mMainActivity.getFileExtension(behindUri));
        fileReference_behind.putFile(behindUri)
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
                        //Toast.makeText(getActivity(), "Upload image failed", Toast.LENGTH_SHORT).show();
                    }
                });

//        Intent intent = new Intent(getActivity(), ValidatePhoneActivity.class);
//        intent.putExtra("phone", strPhone);
//        startActivity(intent);

    }



    private void setUserInformation()
    {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        mDb.setFirestoreSettings(settings);
        DocumentReference newUserRef = mDb
                .collection(getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().getUid());
        newUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mUser = documentSnapshot.toObject(User.class);
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                bindingDataUser(mUser);
            }
        });
    }

    private void bindingDataUser(User user) {

        edtUserName.setText(user.getUsername().toString().trim());
        edtPhone.setText(user.getPhoneNumber().toString().trim());
        edtEmail.setText(user.getEmail().toString().trim());
        edtLicense.setText(user.getDriverLicense().toString().trim());
        edtStreet.setText(user.getStreet().toString().trim());
        edtCity.setText(user.getCity().toString().trim());
        edtPostalCode.setText(user.getPostalCode().toString().trim());
        tvBirthDate.setText(user.getDateOfBirth().toString().trim());
        mAvatarUri = Uri.parse(user.getAvatarURL());
        frontUri = Uri.parse(user.getCiCardFront());
        behindUri = Uri.parse(user.getCiCardBehind());
        Glide.with(getActivity()).load(user.getAvatarURL()).error(R.drawable.ic_avatar_default).into(imgAvatar);
        Glide.with(getActivity()).load(user.getCiCardFront()).into(ivFrontCiCard);
        Glide.with(getActivity()).load(user.getCiCardBehind()).into(ivBehindCiCard);
    }

    private void initUI() {
        imgAvatar = mView.findViewById(R.id.img_avatar_profile_fragment);
        edtUserName = mView.findViewById(R.id.edtUserName);
        edtPhone = mView.findViewById(R.id.edtPhone);
        edtEmail = mView.findViewById(R.id.edtEmail);
        edtLicense = mView.findViewById(R.id.edtLicense);
        edtStreet = mView.findViewById(R.id.edtStreet);
        edtCity = mView.findViewById(R.id.edtCity);
        edtPostalCode = mView.findViewById(R.id.edtPostalCode);
        tvBirthDate = mView.findViewById(R.id.tvBirthDate);
        ivFrontCiCard = mView.findViewById(R.id.ivFrontCiCard);
        ivBehindCiCard = mView.findViewById(R.id.ivBehindCiCard);
        btnUpdate = mView.findViewById(R.id.btnUpdate);
    }


    public void setBitmapImageView(Bitmap bitmap) {
        imgAvatar.setImageBitmap(bitmap);

    }
}