package sg.com.bigspoon.www.BGDashboard.util;

/**
 * Created by qiaoliang89 on 17/4/15.
 */
public class BGUtils {
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
