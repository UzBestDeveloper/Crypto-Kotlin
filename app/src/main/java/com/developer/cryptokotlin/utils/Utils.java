package com.developer.cryptokotlin.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.TreeMap;

public class Utils {

    public static String formatBalance(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("#,###.##", symbols);
        return df.format(value).replace(",", ".");
    }

    public static String formatUnit(double unit) {
        BigInteger number = BigDecimal.valueOf(unit).toBigInteger();
        return createString(number);
    }

    public static String roundUnit(double unit) {
        BigInteger number = BigDecimal.valueOf(unit).toBigInteger();
        return rounded(number);
    }

    public static String parseEpoch(long epochTime, String pattern) {
        Date date = new Date(epochTime);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
        return formatter.format(date);
    }

    public static String getToday() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c);
    }

    public static String getBeginDate(String dateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = sdf.parse(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, Constants.GRAPHIC_PERIOD);
        Date dateBegin = calendar.getTime();

        return sdf.format(dateBegin);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if(html == null){
            return new SpannableString("");
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    private static final String NAMES[] = new String[]{
            "Thousand",
            "МЛН",
            "МЛРД",
            "Trillion",
            "Quadrillion",
            "Quintillion",
            "Sextillion",
            "Septillion",
            "Octillion",
            "Nonillion",
            "Decillion",
            "Undecillion",
            "Duodecillion",
            "Tredecillion",
            "Quattuordecillion",
            "Quindecillion",
            "Sexdecillion",
            "Septendecillion",
            "Octodecillion",
            "Novemdecillion",
            "Vigintillion",
    };
    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private static final NavigableMap<BigInteger, String> MAP;
    static
    {
        MAP = new TreeMap<>();
        for (int i=0; i<NAMES.length; i++)
        {
            MAP.put(THOUSAND.pow(i+1), NAMES[i]);
        }
    }

    public static String createString(BigInteger number) {
        Map.Entry<BigInteger, String> entry = MAP.floorEntry(number);
        if (entry == null)
        {
            return " ";
        }
        return " "+entry.getValue();
    }

    public static String rounded(BigInteger number) {
        Map.Entry<BigInteger, String> entry = MAP.floorEntry(number);
        if (entry == null)
        {
            return "";
        }
        BigInteger key = entry.getKey();
        BigInteger d = key.divide(THOUSAND);
        BigInteger m = number.divide(d);
        double amount = m.doubleValue() / 1000;
        return formatBalance(amount);
    }

}