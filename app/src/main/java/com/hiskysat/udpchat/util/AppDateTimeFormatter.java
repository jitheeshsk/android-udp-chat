package com.hiskysat.udpchat.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppDateTimeFormatter {

    private final String APP_DATE_FORMAT = "dd/MM/yyyy";

    private final Map<String, SimpleDateFormat> dateTimeCache = new HashMap<>();

    private static AppDateTimeFormatter formatter;

    private AppDateTimeFormatter() {

    }

    public static AppDateTimeFormatter getInstance() {
        if (formatter == null) {
            formatter = new AppDateTimeFormatter();
        }
        return formatter;
    }

    public String formatDate(@NonNull Date date) {
        return formatDate(APP_DATE_FORMAT, date);
    }

    public String formatDate(@NonNull String format, @NonNull Date date) {
        return getFormatter(format).format(date);
    }

    @Nullable
    public Date parseDate(@NonNull String date) {
        return parseDate(APP_DATE_FORMAT, date);
    }

    @Nullable
    public Date parseDate(@NonNull String format, @NonNull String date) {
        try {
            return getFormatter(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private SimpleDateFormat getFormatter(String format) {
        SimpleDateFormat formatter = dateTimeCache.get(format);

        if (formatter == null) {
            formatter = new SimpleDateFormat(format, Locale.getDefault());
            dateTimeCache.put(format, formatter);
        }
        return formatter;
    }

}
