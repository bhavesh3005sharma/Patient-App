package com.scout.patient;

import android.app.Application;
import com.scout.patient.Dagger2.DaggerNetworkComponents;
import com.scout.patient.Dagger2.NetworkComponents;
import com.scout.patient.Dagger2.NetworksModule;
import com.scout.patient.Utilities.HelperClass;
import com.squareup.leakcanary.LeakCanary;

public class CustomApplication extends Application {
    private NetworkComponents networkComponents;

    @Override
    public void onCreate() {
        super.onCreate();
        networkComponents = DaggerNetworkComponents.builder()
                .networksModule(new NetworksModule(HelperClass.BASE_URL))
                .build();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    public NetworkComponents getNetworkComponent(){
        return networkComponents;
    }
}
