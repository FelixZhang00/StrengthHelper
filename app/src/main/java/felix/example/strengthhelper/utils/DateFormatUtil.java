package felix.example.strengthhelper.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by felix on 15/2/28.
 */
public class DateFormatUtil {


    /**
     * 将日期转化为制定格式的字符串
     * @param date
     * @param pattern eg.. yyyy-MM-dd
     * @return
     */
    public static String date2String(Date date,String pattern){
        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
