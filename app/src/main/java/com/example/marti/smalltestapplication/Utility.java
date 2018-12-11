package com.example.marti.smalltestapplication;

import android.content.Context;

import org.threeten.bp.LocalDate;
import static org.threeten.bp.temporal.ChronoUnit.DAYS;

public class Utility {
    public static String RelativeTime(Context context, LocalDate then)
    {
        LocalDate now = LocalDate.now();
        return RelativeTime(context, then, now);
    }
    public static String RelativeTime(Context context, LocalDate then, LocalDate now)
    {
        long delta = DAYS.between(then, now);

        if (delta == 0)
        {
            return context.getString(R.string.relativeToday);
        }

        if (delta == 1)
        {
            return context.getString(R.string.relativeYesterday);
        }

        if (delta <= 7)
        {
            return String.format(context.getString(R.string.relativeDays), delta);
        }

        if (delta <= 30)
        {
            return String.format(context.getString(R.string.relativeWeeks), delta/7, (delta < 14 ? "" : "s"));
        }

        if (delta <= 360)
        {
            return String.format(context.getString(R.string.relativeMonths), delta/30, (delta < 60 ? "" : "s"));
        }

        return String.format(context.getString(R.string.relativeYears), delta/360, (delta < 720 ? "" : "s"));
    }
}
