package com.scout.patient.ui.EditProfile;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.Notification.NotificationActivity;
import com.scout.patient.ui.Profile.ProfileActivity;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditProfileActivity extends AppCompatActivity implements Contract.View , View.OnClickListener {
    @BindView(R.id.textViewName)
    TextView textName;
    @BindView(R.id.contactNo)
    TextView textPhoneNo;
    @BindView(R.id.address)
    TextView textAddress;
    @BindView(R.id.medical_history)
    TextView textMedicalHistory;
    @BindView(R.id.blood_group)
    Spinner textBloodGroup;
    @BindView(R.id.weight)
    TextView textWeight;
    @BindView(R.id.dob)
    TextView textDob;
    @BindView(R.id.profileImage)
    CircularImageView profileImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.account_details)
    LinearLayout accountDetails;
    @BindView(R.id.medical_details)
    LinearLayout medicalDetails;
    @BindView(R.id.textInfoMedical)
    TextView textInfoMedical;
    @BindView(R.id.textInfoAccount)
    TextView textInfoAccount;

    Unbinder unbinder;
    ModelPatientInfo patientInfo;
    EditProfilePresenter presenter;
    MaterialDatePicker datePicker;
    TextView gallery,camera,cancel;
    AlertDialog alertDialog;
    Uri imageUri;
    NotificationCompat.Builder notificationBuilder;
    private static final String CHANNEL_ID = "notification_liveTask";
    private static final String CHANNEL_NAME = "CURRENT TASKS";
    private static final String CHANNEL_DESCRIPTION = "Notifications for your tasks in progress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new EditProfilePresenter(EditProfileActivity.this);
        patientInfo = (ModelPatientInfo) getIntent().getSerializableExtra("patientInfo");
        setUpView();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Your DOB");
        datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                textDob.setText(datePicker.getHeaderText());
            }
        });
        profileImage.setOnClickListener(this);
        textDob.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    
    private void setUpView() {
        textName.setText(patientInfo.getName());
        textAddress.setText(patientInfo.getAddress());
        textBloodGroup.setSelection(presenter.getBloodGrpPosition(patientInfo.getBloodGroup()));
        textMedicalHistory.setText(patientInfo.getMedicalHistory());
        textDob.setText(patientInfo.getDOB());
        textWeight.setText(patientInfo.getWeight());
        textPhoneNo.setText(patientInfo.getPhoneNo());
        if (patientInfo.getUrl()!=null)
            Picasso.get().load(Uri.parse(patientInfo.getUrl())).placeholder(R.color.placeholder_bg).into(profileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_icon,menu);

        MenuItem update = menu.findItem(R.id.check_icon);
        update.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String name = textName.getText().toString();
                String phoneNo = textPhoneNo.getText().toString();
                String address = textAddress.getText().toString();
                String dob = textDob.getText().toString();
                String previousDisease = textMedicalHistory.getText().toString();
                String weight = textWeight.getText().toString();
                String bloodGrp;
                if (textBloodGroup.getSelectedItemPosition()==0)
                    bloodGrp = "Not Submitted";
                else
                    bloodGrp = textBloodGroup.getSelectedItem().toString();

                if (name.isEmpty()){
                    textName.setError("Name is Required");
                    textName.requestFocus();
                    return true;
                }else textName.setError(null);

                if (phoneNo.isEmpty()){
                    textPhoneNo.setError("Phone No. is Required");
                    textPhoneNo.requestFocus();
                    return true;
                }else textPhoneNo.setError(null);

                HelperClass.showProgressbar(progressBar);
                String userEmail = SharedPref.getLoginUserData(EditProfileActivity.this).getEmail();
                ModelPatientInfo patientInfo = new ModelPatientInfo(userEmail,weight,name,dob,phoneNo,address,bloodGrp,previousDisease);
                presenter.updateProfileData(patientInfo);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showToast(String s) {
        HelperClass.hideProgressbar(progressBar);
        HelperClass.toast(this,s);
    }

    @Override
    public void setUploadProgress(int progress) {
        notificationBuilder.setProgress(100,progress,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dob:
                if (!datePicker.isAdded())
                    datePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                break;
            case R.id.profileImage :
                if(checkForReadPermission() && checkForWritePermission())
                    openAlertDialogue();
                break;
            case R.id.gallery:
                alertDialog.dismiss();
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                cameraIntent.setType("image/*");
                startActivityForResult(cameraIntent,1);
                break;
            case R.id.camera:
                alertDialog.dismiss();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(EditProfileActivity.this.getPackageManager()) != null)
                    startActivityForResult(takePictureIntent, 2);
                break;
            case R.id.cancel:
                alertDialog.dismiss();
                break;
        }
    }

    private boolean checkForWritePermission() {
        if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2000);
            }
            //Permission automatically granted for Api<23 on installation
        }
        else
            return true;

        return false;
    }

    private boolean checkForReadPermission() {
        if(ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        2000);
            }
            //Permission automatically granted for Api<23 on installation
        }
        else
            return true;

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        alertDialog.dismiss();
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1 && data.getData() != null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).placeholder(R.drawable.ic_person).into(profileImage);
                openAlertDialogueToShowPic();
            } else if (requestCode == 2 && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Log.d("***imageBitmap", imageBitmap + "");
                imageUri = getImageUri(EditProfileActivity.this, imageBitmap);
                openAlertDialogueToShowPic();
                profileImage.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alertDialog!=null)
            alertDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog!=null)
            alertDialog.dismiss();
        unbinder.unbind();
    }

    private void openAlertDialogueToShowPic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.layout_show_profile_image,null));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ImageView imageView = alertDialog.findViewById(R.id.profile_imageToShow);
        TextView confirm = alertDialog.findViewById(R.id.confirm);
        TextView cancel = alertDialog.findViewById(R.id.cancel);

        Picasso.get().load(imageUri).placeholder(R.drawable.ic_person).into(imageView);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveProfilePic(EditProfileActivity.this,imageUri,"ProfilePic." + getFileExtension(imageUri));
                showProgressInNotification();
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void showProgressInNotification() {

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ProfileActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Profile Pic Uploading")
                        .setProgress(100,0,false)
                        .setAutoCancel(false)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(contentIntent);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(this);

        // Since android Oreo notification channel is needed.

        notificationManagerCompat.notify((int) Calendar.getInstance().getTimeInMillis(), notificationBuilder.build());
        presenter.getProgress();
    }

    private void openAlertDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialogue_choose,null));
        alertDialog = builder.create();
        alertDialog.show();

        gallery = alertDialog.findViewById(R.id.gallery);
        camera =  alertDialog.findViewById(R.id.camera);
        cancel = alertDialog.findViewById(R.id.cancel);

        gallery.setOnClickListener(this);
        camera.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = EditProfileActivity.this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
