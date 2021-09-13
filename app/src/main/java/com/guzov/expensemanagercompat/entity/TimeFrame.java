package com.guzov.expensemanagercompat.entity;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public enum TimeFrame {
    FROM_TODAY(getToday(), getToday()),
    FROM_CURRENT_MONTH(getFirstDayOfMonth(), getToday()),
    FROM_PREVIOUS_MONTH_TO_CURRENT_MONTH(getFirstDayOfPreviousMonth(), getFirstDayOfMonth()),
    FROM_CURRENT_YEAR(getFirstDayOfYear(), getToday()),
    WHOLE_TIME(null, getToday());

    TimeFrame(Date fromDate, Date tillDate) {
        this.fromDate = fromDate;
        this.tillDate = tillDate;
    }

    private Date fromDate;
    private Date tillDate;

    public Date getFromDate() {
        return fromDate;
    }

    public Date getTillDate() {
        return tillDate;
    }

    private static Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTime();
    }

    private static Date getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    private static Date getFirstDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }

    private static Date getFirstDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    private static Date getFirstDayOfPreviousMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        int currentMonth = cal.get(Calendar.MONTH);
        if (currentMonth == 0) {
            int previousYear = cal.get(Calendar.YEAR) - 1;
            cal.set(Calendar.YEAR, previousYear);
            cal.set(Calendar.MONTH, 11);
        } else {
            int previousMonth = cal.get(Calendar.MONTH) - 1;
            cal.set(Calendar.MONTH, previousMonth);
        }
        cal.set(Calendar.DAY_OF_MONTH,1);
        return cal.getTime();
    }
}
