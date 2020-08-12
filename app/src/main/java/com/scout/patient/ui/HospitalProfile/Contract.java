package com.scout.patient.ui.HospitalProfile;

import com.scout.patient.Models.ModelHospitalInfo;

public class Contract {
    interface  View{

        void onSetUi(ModelHospitalInfo data);

        void onError(String toString);

    }
    interface  Presenter{

        void getHospitalDetails(String hospitalId);

        void onSuccess(ModelHospitalInfo body);

        void onError(String toString);
    }
    interface Model{
        void getHospitalDetails(String hospitalId);
    }
}
