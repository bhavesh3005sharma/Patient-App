package com.scout.patient;

import android.app.Application;
import com.scout.patient.Utilities.HelperClass;
import com.squareup.leakcanary.LeakCanary;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
