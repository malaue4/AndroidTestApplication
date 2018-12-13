package com.example.marti.smalltestapplication;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.webkit.WebView;

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
            return context != null ? context.getString(R.string.relativeToday) : "Today";
        }

        if (delta == 1)
        {
            return context != null ? context.getString(R.string.relativeYesterday) : "Yesterday";
        }

        if (delta <= 7)
        {
            return String.format(context != null ? context.getString(R.string.relativeDays) : "%d days ago", delta);
        }

        if (delta <= 30)
        {
            return String.format(context != null ? context.getString(R.string.relativeWeeks) : "%d week%s ago", delta / 7, (delta < 14 ? "" : "s"));
        }

        if (delta <= 360)
        {
            return String.format(context != null ? context.getString(R.string.relativeMonths) : "%d month%s ago", delta / 30, (delta < 60 ? "" : "s"));
        }

        return String.format(context != null ? context.getString(R.string.relativeYears) : "%d year%s ago", delta / 360, (delta < 720 ? "" : "s"));
    }


    @BindingAdapter("android:data")
    public static void setData(WebView view, String data) {
        view.getSettings().setLoadWithOverviewMode(true);
        view.getSettings().setUseWideViewPort(true);
        view.loadData(data,
                "text/html", "UTF-8");
    }
}
