package com.zjy.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyCustomDateEditor extends PropertyEditorSupport {
    public final static String DATEFORMAT = "yyyy-MM-dd";
    public final static String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public final static String GMT = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setAsText(String text) {
        Date date = parse(text);
        if (date == null) {
            logger.error("解析Date失败！", text);
        }
        setValue(date);
    }

    public static Date parse(String text) {
        Date date = null;

        try {
            date = getUtcSfd().parse(text);
            return date;
        } catch (Exception e) {
        }
        try {
            date = getGmtSdf().parse(text);
            return date;
        } catch (Exception e) {
        }
        try {
            date = getDateTimeSdf().parse(text);
            return date;
        } catch (Exception e) {
        }
        try {
            date = getDateSdf().parse(text);
            return date;
        } catch (Exception e) {
        }
//        for (DateFormat dateFormat : sdf) {
//            try {
//                date = dateFormat.parse(text);
//                break;
//            } catch (Exception e) {
//            }
//        }
        return date;
    }

    public static DateFormat getDateSdf() {
        return new SimpleDateFormat(DATEFORMAT);
    }

    public static DateFormat getDateTimeSdf() {
        return new SimpleDateFormat(DATETIMEFORMAT);
    }

    public static DateFormat getUtcSfd() {
        DateFormat utcSfd = new SimpleDateFormat(UTC);
        utcSfd.setTimeZone(TimeZone.getTimeZone("UTC"));
        return utcSfd;
    }

    public static DateFormat getGmtSdf() {
        return new SimpleDateFormat(GMT, Locale.US);
    }
}
