package com.scout.patient.Dagger2;

import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {NetworksModule.class})
public interface NetworkComponents {
    void injectLoginActivity(LoginActivity loginActivity);
}
