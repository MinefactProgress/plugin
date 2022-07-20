package de.minefactprogress.progressplugin.utils.time;

public class TimeCalculator {

    private final long SECONDS = 1000L;
    private final long MINUTE = 60 * SECONDS;
    private final long HOUR = 60 * MINUTE;
    private final long DAY = 24 * HOUR;
    private final long MONTH = 30 * DAY;
    private final long YEAR = 12 * MONTH;

    private final long millis;

    public TimeCalculator(long millis) {
        this.millis = millis;
    }

    public String formatTimeAgo() {
        long dif = System.currentTimeMillis() - millis;

        if (dif <= 0) return "Now";

        long rest = dif;
        int year = 0, month = 0, day = 0, hour = 0, min = 0, sec = 0;

        while (rest >= SECONDS) {
            if (rest >= YEAR) {
                rest -= YEAR;
                year++;
            } else if (rest >= MONTH) {
                rest -= MONTH;
                month++;
            } else if (rest >= DAY) {
                rest -= DAY;
                day++;
            } else if (rest >= HOUR) {
                rest -= HOUR;
                hour++;
            } else if (rest >= MINUTE) {
                rest -= MINUTE;
                min++;
            } else {
                rest -= SECONDS;
                sec++;
            }
        }

        String str = "";
        if (year > 0) {
            if (year == 1) {
                str = str + year + " Year";
            } else {
                str = str + year + " Years";
            }
        }
        if (month > 0) {
            if (!str.isEmpty()) {
                str = str + ", ";
            }
            if (month == 1) {
                str = str + month + " Month";
            } else {
                str = str + month + " Months";
            }
        }
        if (day > 0) {
            if (!str.isEmpty()) {
                str = str + ", ";
            }
            if (day == 1) {
                str = str + day + " Day";
            } else {
                str = str + day + " Days";
            }
        }
        if (hour > 0) {
            if (!str.isEmpty()) {
                str = str + ", ";
            }
            if (hour == 1) {
                str = str + hour + " Hour";
            } else {
                str = str + hour + " Hours";
            }
        }
        if (min > 0) {
            if (!str.isEmpty()) {
                str = str + ", ";
            }
            if (min == 1) {
                str = str + min + " Minute";
            } else {
                str = str + min + " Minutes";
            }
        }
        if (sec > 0) {
            if (!str.isEmpty()) {
                str = str + ", ";
            }
            if (sec == 1) {
                str = str + sec + " Second";
            } else {
                str = str + sec + " Seconds";
            }
        }
        if (str.equals("")) return "Now";
        return str + " ago";
    }
}
