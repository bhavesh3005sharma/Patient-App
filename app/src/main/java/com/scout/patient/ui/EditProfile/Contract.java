package com.scout.patient.ui.EditProfile;

import android.net.Uri;

import com.scout.patient.Models.ModelPatientInfo;

public class Contract {
    interface View {

        void showToast(String s);

        void setUploadProgress(int progress);
    }

    interface Presenter {

        int getBloodGrpPosition(String bloodGroup);

        void updateProfileData(ModelPatientInfo patientInfo);

        void showToast(String s);

        void saveProfilePic(EditProfileActivity editProfileActivity, Uri imageUri, String s);

        void getProgress();
    }
    
    interface Model {

        void updateProfileData(ModelPatientInfo patientInfo);

        void updateProfilePic(EditProfileActivity editProfileActivity, String url);
    }
}
