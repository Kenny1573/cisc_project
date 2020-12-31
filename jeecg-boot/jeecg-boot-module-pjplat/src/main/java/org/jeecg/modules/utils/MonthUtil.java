package org.jeecg.modules.utils;

import io.swagger.models.auth.In;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;


public class MonthUtil {





    //根据一个时间获取当前周的周一的日期
    public static String getWeekMonDay(Date time){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if(1==dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,calendar.getFirstDayOfWeek()-day);
        return sdf2.format(calendar.getTime());
    }
    //根据一个时间获取当前周的周日的日期
    public static String getWeekSunDay(Date time){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if(1==dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,calendar.getFirstDayOfWeek()-day);
        calendar.add(Calendar.DATE,6);
        return sdf2.format(calendar.getTime());

    }


    //根据一个时间获取当月的第一天
    public static String getMonthFirstDay(String day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(day.substring(0,4)),Integer.parseInt(day.substring(5,7))-1,1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

    }

    //根据一个时间获取当月的最后一天
    public static String getMonthLastDay(String day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(day.substring(0,4)),Integer.parseInt(day.substring(5,7)),1);
        calendar.add(Calendar.DATE,-1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

    }
    public static String monthTranEnglish(String month) {
        switch (month) {
            case "1月":
                return "Jan";
            case "2月":
                return "Feb";
            case "3月":
                return "Mar";
            case "4月":
                return "Apr";
            case "5月":
                return "May";
            case "6月":
                return "Jun";
            case "7月":
                return "Jul";
            case "8月":
                return "Aug";
            case "9月":
                return "Sep";
            case "10月":
                return "Oct";
            case "11月":
                return "Nov";
            case "12月":
                return "Dec";

        }
        return month;

    }

    public static String monthTranNumber(String month) {
        switch (month) {
            case "Jan":
                return "1月";
            case "Feb":
                return "2月";
            case "Mar":
                return "3月";
            case "Apr":
                return "4月";
            case "May":
                return "5月";
            case "Jun":
                return "6月";
            case "Jul":
                return "7月";
            case "Aug":
                return "8月";
            case "Sep":
                return "9月";
            case "Oct":
                return "10月";
            case "Nov":
                return "11月";
            case "Dec":
                return "12月";
        }
        return "";

    }

    public static Integer monthTranInteger(String month) {
        switch (month) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
        }
        return null;

    }

    public static String getNextMonth(String month) {
        switch (month) {
            case "1":
                return "2";
            case "2":
                return "3";
            case "3":
                return "4";
            case "4":
                return "5";
            case "5":
                return "6";
            case "6":
                return "7";
            case "7":
                return "8";
            case "8":
                return "9";
            case "9":
                return "10";
            case "10":
                return "11";
            case "11":
                return "12";
            case "12":
                return "1";
        }
        return "";

    }

    public static String getLastMonth(String month) {
        switch (month) {
            case "Jan":
                return "Dec";
            case "Feb":
                return "Jan";
            case "Mar":
                return "Feb";
            case "Apr":
                return "Mar";
            case "May":
                return "Apr";
            case "Jun":
                return "May";
            case "Jul":
                return "Jun";
            case "Aug":
                return "Jul";
            case "Sep":
                return "Aug";
            case "Oct":
                return "Sep";
            case "Nov":
                return "Oct";
            case "Dec":
                return "Nov";
        }
        return "";

    }
}
