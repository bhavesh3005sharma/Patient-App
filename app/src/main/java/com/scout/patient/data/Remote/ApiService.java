package com.scout.patient.data.Remote;

import com.scout.patient.Utilities.HelperClass;

public class ApiService {
    private ApiService() {}

    public static RetrofitNetworkApi getAPIService() {
        return RetrofitClient.getClient(HelperClass.BASE_URL).create(RetrofitNetworkApi.class);
    }
}
