package com.example.marti.smalltestapplication;

import java.time.LocalDate;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

public class Utility {
    public static String RelativeTime(LocalDate then)
    {
        LocalDate now = LocalDate.now();
        return RelativeTime(then, now);
    }
    public static String RelativeTime(LocalDate then, LocalDate now)
    {
        long delta = DAYS.between(then, now);

        if (delta == 0)
        {
            return "Today";
        }

        if (delta == 1)
        {
            return "Yesterday";
        }

        if (delta <= 7)
        {
            return String.format("%d days ago", delta);
        }

        if (delta <= 30)
        {
            return String.format("%d week%s ago", delta/7, (delta < 14 ? "" : "s"));
        }

        if (delta <= 360)
        {
            return String.format("%d month%s ago", delta/30, (delta < 60 ? "" : "s"));
        }

        return String.format("%d year%s ago", delta/360, (delta < 720 ? "" : "s"));
    }
}
