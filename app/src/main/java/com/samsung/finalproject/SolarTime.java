package com.samsung.finalproject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class SolarTime {
    private double longitude;
    private LocalDateTime solarTime;
    long hours, minutes, seconds;

    public SolarTime (double longitude) {
        this.longitude = longitude;
        long geoDegrees, geoMinutes, geoSeconds;

        geoDegrees = (int) longitude;
        longitude -= geoDegrees;
        longitude *= 60;
        geoMinutes = (int) longitude;
        longitude -= geoMinutes;
        longitude *= 60;
        geoSeconds = (int) longitude;

        seconds = geoDegrees * 60 * 4;
        seconds += geoMinutes * 4;
        seconds += geoSeconds / 15;
        hours = seconds / 3600;
        seconds %= 3600;
        minutes = seconds / 60;
        seconds %= 60;
    }

    public LocalDateTime getSolarTime() {
        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);

        solarTime = LocalDateTime.of(nowUTC.getYear(), nowUTC.getMonth(), nowUTC.getDayOfMonth(), nowUTC.getHour(), nowUTC.getMinute());
        solarTime = solarTime.plusSeconds(nowUTC.getSecond());
        solarTime = solarTime.plusHours(hours);
        solarTime = solarTime.plusMinutes(minutes);
        solarTime = solarTime.plusSeconds(seconds);

        return solarTime;
    }

    public LocalDateTime getSolarTime(double longitude) {
        long geoDegrees, geoMinutes, geoSeconds, hours, minutes, seconds;

        geoDegrees = (int) longitude;
        longitude -= geoDegrees;
        longitude *= 60;
        geoMinutes = (int) longitude;
        longitude -= geoMinutes;
        longitude *= 60;
        geoSeconds = (int) longitude;

        seconds = geoDegrees * 60 * 4;
        seconds += geoMinutes * 4;
        seconds += geoSeconds / 15;
        hours = seconds / 3600;
        seconds %= 3600;
        minutes = seconds / 60;
        seconds %= 60;

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);

        solarTime = LocalDateTime.of(nowUTC.getYear(), nowUTC.getMonth(), nowUTC.getDayOfMonth(), nowUTC.getHour(), nowUTC.getMinute());
        solarTime = solarTime.plusSeconds(nowUTC.getSecond());
        solarTime = solarTime.plusHours(hours);
        solarTime = solarTime.plusMinutes(minutes);
        solarTime = solarTime.plusSeconds(seconds);

        return this.solarTime;
    }

    public String toString() {
        String hours, minutes, seconds;
        LocalDateTime solarTime = this.getSolarTime();

        if (0 <= solarTime.getHour() && solarTime.getHour() <= 9) {
            hours = "0" + solarTime.getHour();
        } else hours = "" + solarTime.getHour();

        if (0 <= solarTime.getMinute() && solarTime.getMinute() <= 9) {
            minutes = "0" + solarTime.getMinute();
        } else minutes = "" + solarTime.getMinute();

        if (0 <= solarTime.getSecond() && solarTime.getSecond() <= 9) {
            seconds = "0" + solarTime.getSecond();
        } else seconds = "" + solarTime.getSecond();

        return hours + ":" + minutes + ":" + seconds;
    }

}
