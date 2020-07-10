package com.scout.patient.Retrofit;

import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Utilities.HelperClass;

public class ApiService {
    private ApiService() {}

    public static RetrofitNetworkApi getAPIService() {
        return RetrofitClient.getClient(HelperClass.BASE_URL).create(RetrofitNetworkApi.class);
    }
}
