package com.scout.patient.ui.AppointmentBooking;

import android.util.Log;

import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDateTime;
import com.scout.patient.Models.ModelDoctorInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookAppointmentPresenter implements Contract.Presenter {
    Contract.View mainView;
    Model model;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    List<Calendar> availabilityDates = new ArrayList<>();
    List<Calendar> unAvailabilityDates = new ArrayList<>();

    public BookAppointmentPresenter(Contract.View mainView) {
        this.mainView = mainView;
        this.model = new Model(BookAppointmentPresenter.this);
    }

    @Override
    public void bookAppointment(ModelBookAppointment appointment) {
        model.bookAppointment(appointment);
    }

    @Override
    public void OnResponse(String message) {
        mainView.OnResponse(message);
    }

    @Override
    public Calendar[] getAvailabilityDates(ModelDoctorInfo doctorProfileInfo) {
        availabilityDates.clear();
        if (doctorProfileInfo.getAvailabilityType().equals("Monthly")) {
            Calendar calendar = Calendar.getInstance();
            int numberOfMonths = 0;

            while (numberOfMonths!=2) {
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
                    addDateToList(true,a);
                }
                Log.d("Year",""+calendar.get(Calendar.YEAR)+" Month : "+calendar.get(Calendar.MONTH));
                numberOfMonths++;
                if (calendar.get(Calendar.MONTH)== Calendar.JANUARY)
                    calendar.roll(Calendar.YEAR,1);
            }
        }

        if (doctorProfileInfo.getAvailabilityType().equals("Weekly")) {
            Calendar calendar = Calendar.getInstance();

            String takeAppointments = doctorProfileInfo.getSchedule();
            if (takeAppointments==null)
                takeAppointments = "Monthly";
            switch (takeAppointments) {
                case "Weekly":
                    calendar.add(Calendar.WEEK_OF_MONTH, 1);
                    break;
                case "Daily":
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                default:
                    calendar.add(Calendar.MONTH, 1);
                    break;
            }

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

                        addDateToList(true, a);
                    }
                }
                calendar.add(Calendar.DAY_OF_YEAR,1);
                check = calendar.getTime().compareTo(endDate);
            }
        }

        return availabilityDates.toArray(new Calendar[availabilityDates.size()]);
    }

    @Override
    public Calendar[] getCompletelySlotUnavailableDates(ArrayList<ModelDateTime> completelyUnavailableDates) {
        unAvailabilityDates.clear();
        for(ModelDateTime dateTime : completelyUnavailableDates){
            addDateToList(false,dateTime.getDate());
        }
        return unAvailabilityDates.toArray(new Calendar[unAvailabilityDates.size()]);
    }

    @Override
    public void getAppointmentDates(ModelDoctorInfo doctorProfileInfo) {
        model.getUnavailableDates(doctorProfileInfo);
    }

    @Override
    public void setUpDatePicker(ArrayList<ModelDateTime> unavailableDates, ArrayList<ModelDateTime> completelyUnavailableDates, ArrayList<ModelDateTime> partiallyUnavailableDates, ModelDoctorInfo doctorProfileInfo) {
        mainView.setUpDatePicker(getAvailabilityDates(doctorProfileInfo),getCompletelySlotUnavailableDates(completelyUnavailableDates),partiallyUnavailableDates);
    }

    @Override
    public long getThresholdLimit(String time, String checkUpTime) {
        int timeDifference = getTimeDifference(time);
        int avgCheckupTime = Integer.valueOf(checkUpTime);
        return (long) timeDifference/avgCheckupTime;
    }

    private int getTimeDifference(String s) {
        String[] result,time1,time2;
        result = s.split("-");
        time1 = result[0].split(":");
        time2 = result[1].split(":");
        time1[0] = time1[0].replaceAll("\\s+", "");
        time2[0] = time2[0].replaceAll("\\s+", "");
        time1[1] = time1[1].replaceAll("\\s+", "");
        time2[1] = time2[1].replaceAll("\\s+", "");
        int h1,h2,m1,m2;
        h1 = Integer.valueOf(time1[0]);
        h2 = Integer.valueOf(time2[0]);
        m1 = Integer.valueOf(time1[1]);
        m2 = Integer.valueOf(time2[1]);

        if(h2>h1 && m2<m1){
            h2--;
            m2+=60;
        }
        return ((h2-h1)*60)+(m2-m1);
    }

    private void addDateToList(Boolean isAvailabilityDate, String a) {
        java.util.Date date = new Date();

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
            if (isAvailabilityDate)
             availabilityDates.add(cal);
            else
                unAvailabilityDates.add(cal);
        }
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
