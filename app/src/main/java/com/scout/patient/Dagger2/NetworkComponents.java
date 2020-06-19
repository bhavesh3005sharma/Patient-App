package com.scout.patient.Dagger2;

import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;
import com.scout.patient.ui.Auth.Registration.RegistrationActivity;
import com.scout.patient.ui.DoctorsActivity.DoctorsActivity;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {NetworksModule.class})
public interface NetworkComponents {

    void injectLoginActivity(LoginActivity loginActivity);

    void injectRegistrationActivity(RegistrationActivity registrationActivity);

    void injectDoctorsActivity(DoctorsActivity doctorsActivity);
}
