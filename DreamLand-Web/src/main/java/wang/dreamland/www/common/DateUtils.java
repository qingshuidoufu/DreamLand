package wang.dreamland.www.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wly on 2018/2/27.
 */
public class DateUtils {
    public static Date StringToDate(String dateStr, String formatStr){
        DateFormat dd=new SimpleDateFormat(formatStr);
        Date date=null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}