package com.scout.patient.ui.AppointmentBooking;

import android.util.Log;
import android.widget.ProgressBar;

import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Utilities.HelperClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAppointmentPresenter implements Contract.Presenter {
    Contract.View mainView;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    List<Calendar> dates = new ArrayList<>();

    public BookAppointmentPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void bookAppointment(Call<ResponseBody> call, BookAppointmentActivity bookAppointmentActivity, ProgressBar progressBar) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HelperClass.hideProgressbar(progressBar);
                if (response.isSuccessful() && response.code()==200)
                    HelperClass.toast(bookAppointmentActivity,"Appointment Saved Successfully\n You will be notified soon.");
                else
                    HelperClass.toast(bookAppointmentActivity,response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                HelperClass.toast( bookAppointmentActivity,t.getMessage());
                HelperClass.hideProgressbar(progressBar);
            }
        });
    }

    @Override
    public Calendar[] getAvailabilityDates(ModelDoctorInfo doctorProfileInfo) {
        dates.clear();
        if (doctorProfileInfo.getAvailabilityType().equals("Monthly")) {
            Calendar calendar = Calendar.getInstance();
            int numberOfMonths = 0;
            while (numberOfMonths!=3) {
                calendar.roll(Calendar.MONTH,1);
                for (String day : doctorProfileInfo.getDoctorAvailability()) {
                    if (day.length() == 1)
                        day = "0" + day;

                    String month = "" +calendar.get(Calendar.MONTH);
                    if (month.equals("0"))
                        month = "12";
                    if (month.length()==1)
                        month = "0"+ month;

                    String a = day + "-" + month + "-"+calendar.get(Calendar.YEAR);
                    addDateToList(a);
                }
                Log.d("Year",""+calendar.get(Calendar.YEAR)+" Month : "+calendar.get(Calendar.MONTH));
                numberOfMonths++;
                if (calendar.get(Calendar.MONTH)== Calendar.JANUARY)
                    calendar.roll(Calendar.YEAR,1);
            }
        }

        if (doctorProfileInfo.getAvailabilityType().equals("Weekly")) {
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.MONTH,2);
            Date endDate = calendar.getTime();
            calendar = Calendar.getInstance();
            int check = calendar.getTime().compareTo(endDate);
            while (check!=1){
                for (String dayOfWeek : doctorProfileInfo.getDoctorAvailability()) {
                    int week_day = getDayOfWeek(dayOfWeek);
                    if (week_day == calendar.get(Calendar.DAY_OF_WEEK)) {
                        String day = ""+calendar.get(Calendar.DAY_OF_MONTH);
                        if (day.length() == 1)
                            day = "0" + day;

                        String month = "" +(calendar.get(Calendar.MONTH)+1);
                        if (month.length()==1)
                            month = "0"+ month;

                        String a = day + "-" + month + "-"+calendar.get(Calendar.YEAR);

                        addDateToList(a);
                    }
                }
                calendar.add(Calendar.DAY_OF_YEAR,1);
                check = calendar.getTime().compareTo(endDate);
            }
        }

        return dates.toArray(new Calendar[dates.size()]);
    }

    private void addDateToList(String a) {
        java.util.Date date = null;

        boolean status = false;
        sdf.setLenient(false);
        try {
            date = sdf.parse(a);
            status = true;
        } catch (ParseException e) {
            status = false;
            e.printStackTrace();
        }
        if (status) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            dates.add(cal);
        }
        Log.d("WeekDays","DateAdded : "+a);
    }

    private int getDayOfWeek(String dayOfWeek) {
        if (dayOfWeek.equals("Sunday"))
            return Calendar.SUNDAY;
        if (dayOfWeek.equals("Monday"))
            return Calendar.MONDAY;
        if (dayOfWeek.equals("Tuesday"))
            return Calendar.TUESDAY;
        if (dayOfWeek.equals("Wednesday"))
            return Calendar.WEDNESDAY;
        if (dayOfWeek.equals("Thursday"))
            return Calendar.THURSDAY;
        if (dayOfWeek.equals("Friday"))
            return Calendar.FRIDAY;
        if (dayOfWeek.equals("Saturday"))
            return Calendar.SATURDAY;
        return 100;
    }
}
